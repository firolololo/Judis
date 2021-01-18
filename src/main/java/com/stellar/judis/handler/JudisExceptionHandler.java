package com.stellar.judis.handler;


import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/18 17:56
 */
public class JudisExceptionHandler extends ChannelDuplexHandler {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
