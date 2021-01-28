package com.stellar.judis.handler;

import com.stellar.judis.rpc.*;
import com.stellar.judis.server.Master;
import com.stellar.judis.server.core.CoreOperation;
import com.stellar.judis.util.JsonUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.apache.thrift.TException;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/4 14:43
 */
public class SentinelOtherNodeHandler implements SentinelOtherNode.Iface {
    private static final InternalLogger LOG = InternalLoggerFactory.getInstance(SentinelOtherNodeHandler.class);
    private Master master;

    public SentinelOtherNodeHandler(Master master) {
        this.master = master;
    }

    @Override
    public Answer ping(Ping message) throws TException {
        LOG.info("Master {} ping success, Sentinel info:{}", master.getId(), message.getBody());
        Answer answer = new Answer();
        answer.setSuccess(true);
        answer.setBody(JsonUtil.jsonToString(master));
        return answer;
    }

    @Override
    public Answer pong(Pong message) throws TException {
        return null;
    }

    @Override
    public Answer instruction(Instruction message) throws TException {
        switch (message.getDirective()) {
            case MASTER:
                System.out.println("master");
                break;
            case UPDATE:
                System.out.println("update");
                break;
            case SNAPSHOT:
                String threadInfo = Thread.currentThread().getName() + "-" + Thread.currentThread().getId();
                LOG.info("Master {} snapshot success, Thread info:{}", master.getId(), threadInfo);
                master.getCoreOperation().snapshot(null);
                break;
        }
        Answer answer = new Answer();
        answer.setSuccess(true);
        return answer;
    }
}
