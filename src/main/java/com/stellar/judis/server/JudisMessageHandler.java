package com.stellar.judis.server;

import com.stellar.judis.model.Message;
import com.stellar.judis.model.ResolverFactory;
import com.stellar.judis.protocol.Protocol;
import com.stellar.judis.protocol.ProtocolEnum;
import com.stellar.judis.protocol.ProtocolFactory;
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
        Protocol protocol = ProtocolFactory.getProtocol(protocolType.getType());
        String instruction = protocol.parse(message.getBody());
        System.out.println(instruction);
        String command = ResolverFactory.getCommand(instruction);
        String[] params = ResolverFactory.getParams(instruction);
        String resp = ResolverFactory.dispatch(command, params);
        System.out.println(resp);
        channelHandlerContext.writeAndFlush(resp);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }
}
