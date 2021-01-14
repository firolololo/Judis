package com.stellar.judis.handler;

import com.stellar.judis.meta.JudisElement;
import com.stellar.judis.rpc.*;
import com.stellar.judis.server.core.CoreOperation;
import org.apache.thrift.TException;

import java.util.List;

/**
 * @author firo
 * @version 1.0
 * @date 2020/12/15 17:47
 */
public class ClientToServerHandler implements ClientToServer.Iface {
    private CoreOperation<String, JudisElement> operation;

    public ClientToServerHandler(CoreOperation<String, JudisElement> operation) {
        this.operation = operation;
    }
    @Override
    public GetResponse getValue(String key) throws TException {
        return null;
    }

    @Override
    public SetResponse setValue(String key, String value, long time, boolean isPresent) throws TException {
        return null;
    }

    @Override
    public List<GetResponse> getValueBatch(List<GetRequest> getRequests) throws TException {
        return null;
    }

    @Override
    public List<SetResponse> setValueBatch(List<SetRequest> setRequests) throws TException {
        return null;
    }
}
