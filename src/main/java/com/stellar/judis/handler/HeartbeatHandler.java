package com.stellar.judis.handler;

import com.stellar.judis.rpc.Heartbeat;
import org.apache.thrift.TException;

/**
 * @author firo
 * @version 1.0
 * @date 2020/12/15 19:33
 */
public class HeartbeatHandler implements Heartbeat.Iface {
    @Override
    public void ping() throws TException {
        System.out.println("ping");
    }
}
