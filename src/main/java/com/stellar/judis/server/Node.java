package com.stellar.judis.server;

import com.stellar.judis.handler.MasterBusinessHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.apache.thrift.TMultiplexedProcessor;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.stellar.judis.Constants.*;
import static io.netty.channel.ChannelOption.SO_BACKLOG;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/4 14:49
 */
public abstract class Node implements INode {
    private static final InternalLogger LOG = InternalLoggerFactory.getInstance(Node.class);
    public String address;
    public int port;
    protected final transient ServerBootstrap serverBootstrap;
    protected transient Channel listenChannel;
    protected transient TMultiplexedProcessor processor;
    private volatile transient AtomicBoolean running = new AtomicBoolean(false);


    protected Node(String address, int port) {
        this.address = address;
        this.port = port;
        serverBootstrap = new ServerBootstrap();
        // init server
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        serverBootstrap.group(bossGroup, workerGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.option(SO_BACKLOG, SOCK_BACKLOG);
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        processor = new TMultiplexedProcessor();
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(MAX_CONTENT_LENGTH, 0, PKG_HEAD_LEN));
                pipeline.addLast("gatewayHandler", new MasterBusinessHandler(processor));
            }
        });
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getId() { return address + ":" + port; }

    public boolean isAlive() { return this.running.get(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return port == node.port &&
                Objects.equals(address, node.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, port);
    }

    public abstract void assemble();

    public abstract boolean isAssemble();

    @Override
    public boolean start() {
        if (!isAssemble()) {
            LOG.error("Please assemble the node first");
            return false;
        }
        if (running.compareAndSet(false, true)) {
            try {
                listenChannel = serverBootstrap.bind(port).sync().channel();
                LOG.info("Finish to startListen. listenPort=" + port);
                return running.get();
            } catch (Exception e) {
                running.compareAndSet(true, false);
                LOG.error("fail to startListen. listenPort=" + port, e);
            }
        }
        return false;
    }

    @Override
    public boolean stop() {
        if (running.compareAndSet(true, false)) {
            try {
                if (listenChannel != null) {
                    listenChannel.disconnect().get();
                    LOG.info("finish to stopListen. listenPort=" + port);
                }
                return running.get();
            } catch (Exception e) {
                running.compareAndSet(false, true);
                LOG.error("Fail to stopListen. listenPort=" + port, e);
            }
        }
        return false;
    }
}
