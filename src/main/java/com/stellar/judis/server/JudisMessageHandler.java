package com.stellar.judis.server;

import com.alibaba.fastjson.JSONObject;
import com.stellar.judis.model.Message;
import com.stellar.judis.protocol.Protocol;
import com.stellar.judis.protocol.ProtocolEnum;
import com.stellar.judis.protocol.ProtocolFactoryBean;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author cloudwalk3212
 * @version 1.0
 * @date 2020/12/3 16:31
 */
public class JudisMessageHandler extends SimpleChannelInboundHandler<Message> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        ProtocolEnum protocolType = message.getProtocolType();
        Protocol protocol = ProtocolFactoryBean.getProtocol(protocolType.getType());
        channelHandlerContext.writeAndFlush(null);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }
}
