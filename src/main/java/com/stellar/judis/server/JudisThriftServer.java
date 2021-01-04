package com.stellar.judis.server;

import com.stellar.judis.handler.ClientToServerHandler;
import com.stellar.judis.handler.HeartbeatHandler;
import com.stellar.judis.handler.ThriftExecutor;
import com.stellar.judis.handler.ThriftHandler;

import com.stellar.judis.rpc.ClientToServer;
import com.stellar.judis.rpc.Heartbeat;
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

import static com.stellar.judis.Constants.*;
import static io.netty.channel.ChannelOption.SO_BACKLOG;

/**
 * @author firo
 * @version 1.0
 * @date 2020/12/15 19:08
 */
public class JudisThriftServer {
    private static final InternalLogger LOG = InternalLoggerFactory.getInstance(ServerBootstrap.class);
    private final int listenPort;
    private final ServerBootstrap serverBootstrap;
    private Channel listenChannel;
    private CoreOperation<String, String> coreOperation;

    public JudisThriftServer(int port) {
        TMultiplexedProcessor processor = new TMultiplexedProcessor();
        coreOperation = new JudisCoreOperation(new AofAdaptor(), true);
        processor.registerProcessor("Command", new ClientToServer.Processor<>(new ClientToServerHandler(coreOperation)));
        processor.registerProcessor("Heartbeat", new Heartbeat.Processor<>(new HeartbeatHandler()));
        this.listenPort = port;

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup);
//        serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(MAX_CONTENT_LENGTH, 0, PKG_HEAD_LEN));
                pipeline.addLast("gatewayHandler", new ThriftHandler(new ThriftExecutor(processor)));
            }
        });
        serverBootstrap.option(SO_BACKLOG, SOCK_BACKLOG);
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        LOG.info("initiate: listenPort=" + listenPort);
    }

    public boolean startListen() {
        try {
            listenChannel = serverBootstrap.bind(listenPort).sync().channel();
            LOG.info("finish to startListen. listenPort=" + listenPort);
            return true;
        } catch (Exception e) {
            LOG.error("fail to startListen. listenPort=" + listenPort, e);
        }
        return false;
    }

    public void stopListen() {
        try {
            if (listenChannel != null) {
                listenChannel.disconnect().get();
                LOG.info("finish to stopListen. listenPort=" + listenPort);
            }
        } catch (Exception e) {
            LOG.error("fail to stopListen. listenPort=" + listenPort, e);
        }
    }

    public static void main(String[] args) {
        JudisThriftServer server = new JudisThriftServer(8222);
        LOG.info("finish to stopListen. listenPort=" + 8222);
        server.startListen();
    }
}
