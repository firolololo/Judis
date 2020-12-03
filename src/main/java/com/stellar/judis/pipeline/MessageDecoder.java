package com.stellar.judis.pipeline;

import com.stellar.judis.model.Message;
import com.stellar.judis.model.MessageTypeEnum;
import com.stellar.judis.protocol.Protocol;
import com.stellar.judis.protocol.ProtocolEnum;
import com.stellar.judis.protocol.ProtocolFactoryBean;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author cloudwalk3212
 * @version 1.0
 * @date 2020/12/3 14:21
 */
public class MessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        Message message = new Message();
        message.setMessageType(MessageTypeEnum.get(byteBuf.readByte()));
        message.setProtocolType(ProtocolEnum.get(byteBuf.readByte()));
        Protocol protocol = ProtocolFactoryBean.getProtocol(message.getProtocolType().getType());
        int idLength = byteBuf.readInt();
        message.setId(byteBuf.readCharSequence(idLength, protocol.charset()).toString());
        int bodyLength = byteBuf.readInt();
        message.setBody(byteBuf.readCharSequence(bodyLength, protocol.charset()).toString());
        list.add(message);
    }
}
