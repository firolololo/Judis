package com.stellar.judis.server.task;

import com.alibaba.fastjson.JSONObject;
import com.stellar.judis.exception.JudisNodeAccessException;
import com.stellar.judis.meta.NodeContext;
import com.stellar.judis.rpc.Answer;
import com.stellar.judis.rpc.Ping;
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
 * @date 2021/1/6 15:16
 */
public class SentinelPingTask implements Runnable {
    private static final InternalLogger LOG = InternalLoggerFactory.getInstance(SentinelPingTask.class);

    private Sentinel sentinel;
    private String masterId;

    public SentinelPingTask(Sentinel sentinel, String masterId) {
        this.sentinel = sentinel;
        this.masterId = masterId;
    }

    @Override
    public void run() {
        NodeContext context = new NodeContext();
        context.setSentinel(sentinel);
        Master target = sentinel.getMaster(masterId);
        TTransport transport = new TFramedTransport(new TSocket(target.getAddress(), target.getPort()));
        try {
            TProtocol protocol = new TMultiplexedProtocol(new TCompactProtocol(transport), "Sentinel");
            SentinelOtherNode.Client client = new SentinelOtherNode.Client(protocol);
            transport.open();
            Ping pingMaster = new Ping();
            pingMaster.setAddress(sentinel.getAddress());
            pingMaster.setPort(sentinel.getPort());
            pingMaster.setBody(JSONObject.toJSONString(sentinel));
            Answer answer = client.ping(pingMaster);
            if (answer == null || !answer.isSuccess()) throw new JudisNodeAccessException();
            String body = answer.getBody();
            Master master = JSONObject.parseObject(body, Master.class);
            context.setMaster(master);
            sentinel.getEventCenter().triggerSuccessEvent(context);
        } catch (Exception e) {
            context.setMaster(sentinel.getMaster(masterId));
            sentinel.getEventCenter().triggerFailEvent(context);
            LOG.error("Ping master node failed: ", e);
        } finally {
            transport.close();
        }
    }
}
