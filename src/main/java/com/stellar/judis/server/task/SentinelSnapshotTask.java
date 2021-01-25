package com.stellar.judis.server.task;

import com.stellar.judis.exception.JudisInstructionException;
import com.stellar.judis.exception.JudisNodeAccessException;
import com.stellar.judis.rpc.Answer;
import com.stellar.judis.rpc.Directive;
import com.stellar.judis.rpc.Instruction;
import com.stellar.judis.rpc.SentinelOtherNode;
import com.stellar.judis.server.Master;
import com.stellar.judis.server.sentinel.Sentinel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/21 16:51
 */
public class SentinelSnapshotTask implements Runnable {
    private static final InternalLogger LOG = InternalLoggerFactory.getInstance(SentinelSnapshotTask.class);

    private Sentinel sentinel;
    private String masterId;

    public SentinelSnapshotTask(Sentinel sentinel, String masterId) {
        this.sentinel = sentinel;
        this.masterId = masterId;
    }

    @Override
    public void run() {
        Master target = sentinel.getMaster(masterId);
        TTransport transport = new TFramedTransport(new TSocket(target.getAddress(), target.getPort()));
        try {
            TProtocol protocol = new TMultiplexedProtocol(new TCompactProtocol(transport), "Sentinel");
            SentinelOtherNode.Client client = new SentinelOtherNode.Client(protocol);
            transport.open();
            Instruction inst = new Instruction();
            inst.setDirective(Directive.SNAPSHOT);
            Answer answer = client.instruction(inst);
            if (answer == null || !answer.isSuccess()) throw new JudisInstructionException();
        } catch (Exception e) {
            LOG.error("Snapshot master node failed: ", e);
        } finally {
            transport.close();
        }
    }
}
