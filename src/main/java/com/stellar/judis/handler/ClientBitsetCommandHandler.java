package com.stellar.judis.handler;

import com.stellar.judis.Constants;
import com.stellar.judis.meta.JudisBitSet;
import com.stellar.judis.meta.JudisElement;
import com.stellar.judis.rpc.BitOption;
import com.stellar.judis.rpc.ClientBitSetCommand;
import com.stellar.judis.rpc.CommandResponse;
import com.stellar.judis.rpc.CommandType;
import com.stellar.judis.server.core.CoreOperation;
import com.stellar.judis.util.TBaseCheckUtil;
import org.apache.thrift.TException;

import java.util.BitSet;
import java.util.List;
import java.util.Optional;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/18 11:37
 */
public class ClientBitsetCommandHandler implements ClientBitSetCommand.Iface {
    private CoreOperation<String, JudisElement> operation;

    public ClientBitsetCommandHandler(CoreOperation<String, JudisElement> operation) {
        this.operation = operation;
    }

    @Override
    public CommandResponse getBit(String key, int offset) throws TException {
        CommandResponse response = new CommandResponse();
        response.setCommandType(CommandType.BITSET);
        try {
            TBaseCheckUtil.checkKeyNotNull(key);
            TBaseCheckUtil.checkOffsetNotLessThanZero(offset);
            JudisElement element = operation.get(key);
            TBaseCheckUtil.checkKeyExisted(element);
            TBaseCheckUtil.checkValueType(element, JudisBitSet.class);
            BitSet bitSet = ((JudisBitSet)element).getBitSet();
            response.setSuccess(true);
            response.setData(String.valueOf(bitSet.get(offset)));
        } catch (TException e) {
            response.setSuccess(false);
            response.setData(e.getMessage());
        }
        return response;
    }

    @Override
    public CommandResponse setBit(String key, int offset, boolean value) throws TException {
        CommandResponse response = new CommandResponse();
        response.setCommandType(CommandType.BITSET);
        try {
            TBaseCheckUtil.checkKeyNotNull(key);
            TBaseCheckUtil.checkOffsetNotLessThanZero(offset);
            JudisElement element = Optional.ofNullable(operation.get(key)).orElse(new JudisBitSet(new BitSet()));
            TBaseCheckUtil.checkValueType(element, JudisBitSet.class);
            BitSet bitSet = ((JudisBitSet)element).getBitSet();
            bitSet.set(offset, value);
            response.setSuccess(true);
            response.setData(Constants.OPERATION_SUCCESS);
        } catch (TException e) {
            response.setSuccess(false);
            response.setData(e.getMessage());
        }
        return response;
    }

    @Override
    public CommandResponse countBit(String key, int start, int stop) throws TException {
        CommandResponse response = new CommandResponse();
        response.setCommandType(CommandType.BITSET);
        try {
            TBaseCheckUtil.checkKeyNotNull(key);
            JudisElement element = operation.get(key);
            TBaseCheckUtil.checkKeyExisted(element);
            TBaseCheckUtil.checkValueType(element, JudisBitSet.class);
            BitSet bitSet = ((JudisBitSet)element).getBitSet();
            if (stop < 0) {
                stop = bitSet.length() + stop;
            }
            TBaseCheckUtil.checkRangeStartAndEndValid(start, stop);
            response.setSuccess(true);
            response.setData(String.valueOf(bitSet.get(start, stop + 1).cardinality()));
        } catch (TException e) {
            response.setSuccess(false);
            response.setData(e.getMessage());
        }
        return response;
    }

    @Override
    public CommandResponse topBit(BitOption option, String key, List<String> keys) throws TException {
        CommandResponse response = new CommandResponse();
        response.setCommandType(CommandType.BITSET);
        BitSet bitSet = null;
        for (String bitKey: keys) {
            JudisElement element = operation.get(bitKey);
            if (element != null && JudisBitSet.class.equals(element.getClass())) {
                BitSet bs = ((JudisBitSet)element).getBitSet();
                if (bitSet == null) {
                    bitSet = new BitSet();
                    bitSet.or(bs);
                } else {
                    bitSet = doBitOptionDispatch(option, bitSet, bs);
                }
            }
        }
        response.setSuccess(true);
        response.setData(Optional.ofNullable(bitSet).map(BitSet::toString).orElse(""));
        return response;
    }

    private BitSet doBitOptionDispatch(BitOption option, BitSet bs1, BitSet bs2) {
        if (bs1 == null) return null;
        if (bs2 == null) return bs1;
        switch (option) {
            case OR:
                bs1.or(bs2);
                break;
            case AND:
                bs1.and(bs2);
                break;
            case NOT:
                bs1.andNot(bs2);
                break;
            case XOR:
                bs1.xor(bs2);
                break;
        }
        return bs1;
    }
}
