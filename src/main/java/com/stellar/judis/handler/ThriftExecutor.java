package com.stellar.judis.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.transport.TByteBuffer;
import org.apache.thrift.transport.TFramedTransport;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author firo
 * @version 1.0
 * @date 2020/12/15 18:47
 */
public class ThriftExecutor {
    private final ExecutorService es = Executors.newFixedThreadPool(10);
    private final TMultiplexedProcessor processor;

    public ThriftExecutor(TMultiplexedProcessor processor) {
        this.processor = processor;
    }

    public void executeAction(ChannelHandlerContext ctx, ByteBuf srcBuf) {
        int len = srcBuf.readableBytes();
        ByteBuffer inBuf = ByteBuffer.allocate(len);
        srcBuf.getBytes(0, inBuf);
        inBuf.flip();

        es.execute(() -> {
            try {
                ByteBuffer outBuf = ByteBuffer.allocate(256);
                processor.process(new TCompactProtocol(new TFramedTransport(new TByteBuffer(inBuf))),
                        new TCompactProtocol(new TFramedTransport(new TByteBuffer(outBuf))));
                outBuf.flip();
                int retLen = outBuf.remaining();
                ByteBuf dstBuf = ctx.alloc().buffer(retLen).writeBytes(outBuf);
                ctx.writeAndFlush(dstBuf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
