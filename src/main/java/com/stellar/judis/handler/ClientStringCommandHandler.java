package com.stellar.judis.handler;

import com.stellar.judis.Constants;
import com.stellar.judis.meta.*;
import com.stellar.judis.rpc.*;
import com.stellar.judis.server.core.CoreOperation;
import com.stellar.judis.util.TBaseCheckUtil;
import org.apache.thrift.TException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/14 15:39
 */
public class ClientStringCommandHandler implements ClientStringCommand.Iface {
    private CoreOperation<String, JudisElement> operation;

    public ClientStringCommandHandler(CoreOperation<String, JudisElement> operation) {
        this.operation = operation;
    }

    @Override
    public CommandResponse getString(String key) throws TException {
        CommandResponse response = new CommandResponse();
        response.setCommandType(CommandType.STRING);
        try {
            TBaseCheckUtil.checkKeyNotNull(key);
            JudisElement element = operation.get(key);
            TBaseCheckUtil.checkKeyExisted(element);
            TBaseCheckUtil.checkValueType(element, JudisString.class);
            response.setSuccess(true);
            response.setData(element.getTypeImpl().serialize());
        } catch (TException e) {
            response.setSuccess(false);
            response.setData(e.getMessage());
        }
        return response;
    }

    @Override
    public CommandResponse setString(String key, String value, long time, boolean ne) throws TException {
        CommandResponse response = new CommandResponse();
        response.setCommandType(CommandType.STRING);
        try {
            TBaseCheckUtil.checkKeyNotNull(key);
            TBaseCheckUtil.checkValueNotNull(value);
            if (ne && operation.containsKey(key)) {
                response.setSuccess(false);
                response.setData(Constants.KEY_EXISTED_ALREADY);
                return response;
            }
            JudisElement element;
            if (time > 0) {
                element = operation.put(key, new JudisString(value), time, TimeUnit.SECONDS);
            } else {
                element = operation.put(key, new JudisString(value));
            }
            response.setSuccess(true);
            String data = Optional.ofNullable(element)
                    .map(JudisElement::getTypeImpl)
                    .map(IJudisElementType::serialize)
                    .orElse("");
            response.setData(data);
        } catch (TException e) {
            response.setSuccess(false);
            response.setData(e.getMessage());
        }
        return response;
    }

    @Override
    public CommandResponse appendString(String key, String value) throws TException {
        CommandResponse response = new CommandResponse();
        response.setCommandType(CommandType.STRING);
        try {
            TBaseCheckUtil.checkKeyNotNull(key);
            TBaseCheckUtil.checkValueNotNull(value);
            if (!operation.containsKey(key)) {
                return setString(key, value, -1, true);
            }
            JudisElement element = operation.get(key);
            TBaseCheckUtil.checkValueNotNull(element);
            TBaseCheckUtil.checkValueType(element, JudisString.class);
            ((JudisString)element).append(value);
            response.setSuccess(true);
            response.setData(Constants.OPERATION_SUCCESS);
        } catch (TException e) {
            response.setSuccess(false);
            response.setData(e.getMessage());
        }
        return response;
    }

    @Override
    public CommandResponse mgetString(List<String> keys) throws TException {
        String[] values = new String[keys.size()];
        int index = 0;
        for (String key: keys) {
            if (key == null) {
                values[index++] = Constants.GET_ERROR;
            } else {
                JudisElement element = operation.get(key);
                if (!(element instanceof JudisString)) {
                    values[index++] = Constants.GET_ERROR;
                } else {
                    values[index++] = element.getTypeImpl().serialize();
                }
            }
        }
        CommandResponse response = new CommandResponse();
        response.setCommandType(CommandType.STRING);
        response.setSuccess(true);
        response.setData(Arrays.toString(values));
        return response;
    }

    @Override
    public CommandResponse msetString(List<StringPair> pairs) throws TException {
        CommandResponse response = new CommandResponse();
        response.setCommandType(CommandType.STRING);
        try {
            for (StringPair pair: pairs) {
                TBaseCheckUtil.checkKeyNotNull(pair.getKey());
                TBaseCheckUtil.checkValueNotNull(pair.getValue());
            }
            for (StringPair pair: pairs) {
                operation.put(pair.getKey(), new JudisString(pair.getValue()));
            }
            response.setSuccess(true);
            response.setData(Constants.OPERATION_SUCCESS);
        } catch (TException e) {
            response.setSuccess(false);
            response.setData(e.getMessage());
        }
        return response;
    }

    @Override
    public CommandResponse incr(String key) throws TException {
        return incrBy(key, 1L);
    }

    @Override
    public CommandResponse incrBy(String key, long increment) throws TException {
        CommandResponse response = new CommandResponse();
        response.setCommandType(CommandType.STRING);
        try {
            TBaseCheckUtil.checkKeyNotNull(key);
            JudisElement element = operation.get(key);
            long value;
            if (element == null) {
                value = 0L;
                operation.put(key, new JudisString(String.valueOf(value + increment)));
            } else {
                TBaseCheckUtil.checkValueType(element, JudisString.class);
                value = TBaseCheckUtil.checkValueLongAndGet(element);
                ((JudisString)element).setValue(String.valueOf(value + increment));
            }
            response.setSuccess(true);
            response.setData(Constants.OPERATION_SUCCESS);
        } catch (TException e) {
            response.setSuccess(false);
            response.setData(e.getMessage());
        }
        return response;
    }

    @Override
    public CommandResponse decr(String key) throws TException {
        return incrBy(key, -1);
    }

    @Override
    public CommandResponse decrBy(String key, long decrement) throws TException {
        return incrBy(key, -1 * decrement);
    }
}
