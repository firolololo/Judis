package com.stellar.judis.util;

import com.stellar.judis.Constants;
import com.stellar.judis.meta.JudisElement;
import org.apache.thrift.TException;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/14 17:21
 */
public class TBaseCheckUtil {
    public static void checkKeyNotNull(String key) throws TException {
        if (key == null) throw new TException(Constants.KEY_NOT_NULL);
    }

    public static void checkKeyExisted(JudisElement value) throws TException {
        if (value == null) throw new TException(Constants.KEY_NOT_EXISTED);
    }

    public static void checkValueNotNull(String value) throws TException {
        if (value == null) throw new TException(Constants.VALUE_NOT_NULL);
    }

    public static void checkValueNotNull(JudisElement value) throws TException {
        if (value == null) throw new TException(Constants.VALUE_NOT_NULL);
    }

    public static void checkOffsetNotLessThanZero(int offset) throws TException {
        if (offset < 0) throw new TException(Constants.OFFSET_LESS_THAN_ZERO);
    }

    public static void checkRangeStartAndEndValid(int start, int end) throws TException {
        if (!(start >= 0 && end >= 0 && end >= start)) throw new TException(Constants.RANGE_START_OR_END_INVALID);
    }

    public static void checkValueType(JudisElement value, Class<? extends JudisElement> clazz) throws TException {
        if (!clazz.equals(value.getClass())) throw new TException(Constants.VALUE_TYPE_NOT_MATCH);
    }

    public static long checkValueLongAndGet(JudisElement value) throws TException {
        try {
            String v = value.getTypeImpl().serialize();
            return Long.parseLong(v);
        } catch (Exception e) {
            throw new TException(Constants.VALUE_NOT_LONG);
        }
    }
}
