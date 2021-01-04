package com.stellar.judis.handler;

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
    private CoreOperation<String, String> operation;

    public ClientToServerHandler(CoreOperation<String, String> operation) {
        this.operation = operation;
    }
    @Override
    public GetResponse getValue(String key) throws TException {
        GetResponse response = new GetResponse();
        try {
            String res = operation.get(key);
            response.setSuccess(true);
            response.setValue(res);
        } catch (Exception e) {
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public SetResponse setValue(String key, String value, long time, boolean isPresent) throws TException {
        String duration = time > 0 ? String.valueOf(time): null;
        SetResponse response = new SetResponse();
        try {
            String res = operation.put(key, value);
            response.setSuccess(true);
            response.setOldValue(res);
        } catch (Exception e) {
            response.setSuccess(false);
        }
        return response;
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
