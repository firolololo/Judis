package com.stellar.judis.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.transport.TByteBuffer;
import org.apache.thrift.transport.TFramedTransport;

import java.nio.ByteBuffer;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/4 15:22
 */
public class MasterBusinessHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private final TMultiplexedProcessor processor;
    private static final int OUT_BUFFER_LENGTH = 256;

    public MasterBusinessHandler(TMultiplexedProcessor processor) {
        this.processor = processor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        int len = byteBuf.readableBytes();
        ByteBuffer inBuf = ByteBuffer.allocate(len);
        byteBuf.getBytes(0, inBuf);
        inBuf.flip();
        try {
            ByteBuffer outBuf = ByteBuffer.allocate(OUT_BUFFER_LENGTH);
            processor.process(new TCompactProtocol(new TFramedTransport(new TByteBuffer(inBuf))),
                    new TCompactProtocol(new TFramedTransport(new TByteBuffer(outBuf))));
            outBuf.flip();
            int retLen = outBuf.remaining();
            ByteBuf dstBuf = channelHandlerContext.alloc().buffer(retLen).writeBytes(outBuf);
            channelHandlerContext.writeAndFlush(dstBuf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
