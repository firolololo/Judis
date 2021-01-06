package com.stellar.judis.handler;

import com.alibaba.fastjson.JSONObject;
import com.stellar.judis.rpc.*;
import com.stellar.judis.server.Master;
import org.apache.thrift.TException;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/4 14:43
 */
public class SentinelOtherNodeHandler implements SentinelOtherNode.Iface {
    private Master master;

    public SentinelOtherNodeHandler(Master master) {
        this.master = master;
    }

    @Override
    public Answer ping(Ping message) throws TException {
        System.out.println(message.getBody());
        Answer answer = new Answer();
        answer.setSuccess(true);
        answer.setBody(JSONObject.toJSONString(master));
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
                System.out.println("snapshot");
                break;
        }
        Answer answer = new Answer();
        answer.setSuccess(true);
        return answer;
    }
}
