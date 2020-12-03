package com.stellar.judis.client;

import com.stellar.judis.model.Message;
import com.stellar.judis.model.MessageTypeEnum;
import com.stellar.judis.pipeline.MessageDecoder;
import com.stellar.judis.pipeline.MessageEncoder;
import com.stellar.judis.protocol.ProtocolEnum;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.URI;

/**
 * @author cloudwalk3212
 * @version 1.0
 * @date 2020/10/7 14:11
 */
public class JudisClient {
    public void connect(String host, int port) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
//                    ch.pipeline().addLast(new HttpResponseDecoder());
                    // 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
//                    ch.pipeline().addLast(new HttpRequestEncoder());
//                    ch.pipeline().addLast(new JudisClientHandler());
                    ch.pipeline().addLast("readTimeoutHandler",
                            new ReadTimeoutHandler(10));
                    ch.pipeline().addLast(new MessageEncoder());
                    ch.pipeline().addLast(new MessageDecoder());
                }
            });
            ChannelFuture f = b.connect(host, port).sync();
//            URI uri = new URI("http://127.0.0.1:5888");
//            String msg = "Are you ok?";
//            DefaultFullHttpRequest request = new DefaultFullHttpRequest(
//                    HttpVersion.HTTP_1_1, HttpMethod.GET, uri.toASCIIString(),
//                    Unpooled.wrappedBuffer(msg.getBytes()));
//            // 构建http请求
//            request.headers().set(HttpHeaderNames.HOST, host);
//            request.headers().set(HttpHeaderNames.CONNECTION,
//                    HttpHeaderNames.CONNECTION);
//            request.headers().set(HttpHeaderNames.CONTENT_LENGTH,
//                    request.content().readableBytes());
//            request.headers().set("messageType", "normal");
//            request.headers().set("businessType", "testServerState");
            // 发送http请求
            Message message = new Message();
            message.setMessageType(MessageTypeEnum.REQUEST);
            message.setProtocolType(ProtocolEnum.JUDIS);
            message.setBody("SET TEST TEST");
            f.channel().writeAndFlush(message);
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        JudisClient client = new JudisClient();
        client.connect("127.0.0.1", 5888);
    }
}
