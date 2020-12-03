package com.stellar.judis.model;

/**
 * @author cloudwalk3212
 * @version 1.0
 * @date 2020/12/3 13:51
 */
public enum MessageTypeEnum {
    REQUEST((byte)1), RESPONSE((byte)2), PING((byte)3), PONG((byte)4), EMPTY((byte)5);

    private byte type;

    MessageTypeEnum(byte type) {
        this.type = type;
    }

    public static MessageTypeEnum get(byte type) {
        for (MessageTypeEnum value: values()) {
            if (value.type == type) return value;
        }
        throw new RuntimeException("unsupported type:" + type);
    }

    public byte getType() {
        return type;
    }
}
