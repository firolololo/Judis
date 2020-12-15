package com.stellar.judis.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author firo
 * @version 1.0
 * @date 2020/12/15 19:02
 */
public class ThriftHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private final ThriftExecutor executor;

    private static final Logger LOG = LoggerFactory.getLogger(ThriftHandler.class);

    public ThriftHandler(ThriftExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String channelId = channel.id().asShortText();
        LOG.trace("channelActive: channelId={}", channelId);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String channelId = channel.id().asShortText();
        LOG.trace("channelInactive: channelId={}", channelId);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        executor.executeAction(channelHandlerContext, byteBuf);
    }
}
