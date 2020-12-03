package com.stellar.judis.server;

import com.stellar.judis.pipeline.MessageDecoder;
import com.stellar.judis.pipeline.MessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.log4j.Logger;

/**
 * @author cloudwalk3212
 * @version 1.0
 * @date 2020/9/25 14:03
 */
public class JudisServer {
    private Logger log = Logger.getLogger(getClass());
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    public void start(int port) throws Exception {
       try {
           ServerBootstrap b = new ServerBootstrap();
           b.group(bossGroup, workerGroup)
                   .handler(new LoggingHandler(LogLevel.DEBUG))
                   .channel(NioServerSocketChannel.class)
                   .childHandler(new ChannelInitializer<SocketChannel>() {
                       @Override
                       public void initChannel(SocketChannel ch) throws Exception {
//                           ch.pipeline().addLast(new HttpResponseEncoder());
//                           ch.pipeline().addLast(new HttpRequestDecoder());
//                           ch.pipeline().addLast(new HttpObjectAggregator(10 * 1024 * 1024));
//                           ch.pipeline().addLast(new JudisServerInHandler());
//                           ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
//                           ch.pipeline().addLast(new LengthFieldPrepender(4));
                           ch.pipeline().addLast(new MessageEncoder());
                           ch.pipeline().addLast(new MessageDecoder());
                           ch.pipeline().addLast(new JudisMessageHandler());
                       }
                   })
                   .option(ChannelOption.SO_BACKLOG, 128)
                   .childOption(ChannelOption.SO_KEEPALIVE, true);
           ChannelFuture f = b.bind(port).sync();
           f.channel().closeFuture().sync();
       } finally {
           bossGroup.shutdownGracefully();
           workerGroup.shutdownGracefully();
       }
    }

    public static void main(String[] args) throws Exception {
        JudisServer server = new JudisServer();
        server.start(5888);
    }
}
