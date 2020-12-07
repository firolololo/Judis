package com.stellar.judis.pipeline;

import com.stellar.judis.model.Message;
import com.stellar.judis.model.MessageTypeEnum;
import com.stellar.judis.protocol.Protocol;
import com.stellar.judis.protocol.ProtocolEnum;
import com.stellar.judis.protocol.ProtocolFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.UUID;

/**
 * @author cloudwalk3212
 * @version 1.0
 * @date 2020/12/3 14:20
 */
public class MessageEncoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        if (message.getMessageType() != MessageTypeEnum.EMPTY) {
            ProtocolEnum protocolType = message.getProtocolType();
            Protocol protocol = ProtocolFactory.getProtocol(protocolType.getType());
            String body = message.getBody();
            byteBuf.writeByte(message.getMessageType().getType());
            byteBuf.writeByte(protocolType.getType());
            String id = UUID.randomUUID().toString();
            byteBuf.writeInt(id.length());
            byteBuf.writeCharSequence(id, protocol.charset());
            if (body == null) {
                byteBuf.writeInt(0);
            } else {
                String content = protocol.encode(body);
                byteBuf.writeInt(content.length());
                byteBuf.writeCharSequence(content, protocol.charset());
            }
        }
    }
}
