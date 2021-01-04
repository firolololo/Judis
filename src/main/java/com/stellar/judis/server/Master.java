package com.stellar.judis.server;

import com.stellar.judis.handler.*;
import com.stellar.judis.rpc.ClientToServer;
import com.stellar.judis.rpc.Heartbeat;
import com.stellar.judis.rpc.SentinelOtherNode;
import com.stellar.judis.server.core.CoreOperation;
import com.stellar.judis.server.core.JudisCoreOperation;
import com.stellar.judis.server.persist.AofAdaptor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.stellar.judis.Constants.*;
import static io.netty.channel.ChannelOption.SO_BACKLOG;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/4 14:52
 */
public class Master extends Node {
    private static final InternalLogger LOG = InternalLoggerFactory.getInstance(Master.class);
    private List<Servant> servantList;
    private TMultiplexedProcessor processor;
    private CoreOperation<String, String> coreOperation;
    private volatile AtomicBoolean completed = new AtomicBoolean(false);

    public Master(String address, int port, CoreOperation<String, String> coreOperation) {
        super(address, port);
        servantList = new LinkedList<>();
        this.coreOperation = coreOperation;
    }

    public Master(String address, int port) {
        super(address, port);
        servantList = new LinkedList<>();
        // init opt
        coreOperation = new JudisCoreOperation(new AofAdaptor(), true);
    }

    public boolean assemble() {
        if (completed.compareAndSet(false, true)) {
            try {
                processor = new TMultiplexedProcessor();
                processor.registerProcessor("Command", new ClientToServer.Processor<>(new ClientToServerHandler(coreOperation)));
                processor.registerProcessor("Heartbeat", new Heartbeat.Processor<>(new HeartbeatHandler()));
                processor.registerProcessor("Sentinel", new SentinelOtherNode.Processor<>(new SentinelOtherNodeHandler()));
                serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(MAX_CONTENT_LENGTH, 0, PKG_HEAD_LEN));
                        pipeline.addLast("gatewayHandler", new MasterBusinessHandler(processor));
                    }
                });
                return completed.get();
            } catch (Exception e) {
                completed.compareAndSet(true, false);
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean addServant(Servant servant) {
        return this.servantList.add(servant);
    }
}
