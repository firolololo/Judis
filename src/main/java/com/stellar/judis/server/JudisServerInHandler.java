package com.stellar.judis.server;

import com.stellar.judis.util.ByteBufToBytes;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @author cloudwalk3212
 * @version 1.0
 * @date 2020/9/25 17:39
 */
public class JudisServerInHandler extends ChannelInboundHandlerAdapter {
    private ByteBufToBytes reader;
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        SocketChannel channel = (SocketChannel) ctx.channel();
        System.out.println("客户端链接到服务端 channelId：" + channel.id()
                + " Ip: " + channel.localAddress().getHostString()
                + " Port：" + channel.localAddress().getPort());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("客户端断开链接：" + ctx.channel().localAddress().toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest httpRequest = (FullHttpRequest) msg;
            String path = httpRequest.uri();
            System.out.println(path);
            String body = getBody(httpRequest);
            HttpMethod method = httpRequest.method();
            if (HttpMethod.GET.equals(method)) {
                System.out.println("服务端接受消息内容：" + body);
                String result = "GET请求测试";
                send(ctx, result, HttpResponseStatus.OK);
            }
            if (HttpMethod.POST.equals(method)) {
                System.out.println("服务端接受消息内容：" + body);
                String result = "POST请求测试";
                send(ctx, result, HttpResponseStatus.OK);
            }
        }

    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private String getBody(FullHttpRequest request) {
        ByteBuf buf = request.content();
        return buf.toString(CharsetUtil.UTF_8);
    }

    private void send(ChannelHandlerContext ctx, String context, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(context, CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
