package com.stellar.judis.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * @author cloudwalk3212
 * @version 1.0
 * @date 2020/10/7 14:04
 */
public class JudisServerOutHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        System.out.println("--------JudisServerOutHandler read--------");
        super.read(ctx);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("--------JudisServerOutHandler write--------");
        super.write(ctx, msg, promise);
    }
}
