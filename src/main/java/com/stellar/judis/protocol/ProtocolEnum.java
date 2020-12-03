package com.stellar.judis.protocol;

/**
 * @author cloudwalk3212
 * @version 1.0
 * @date 2020/12/3 14:48
 */
public enum ProtocolEnum {
    JUDIS((byte)1);

    private byte type;

    ProtocolEnum(byte type) {this.type = type;}

    public static ProtocolEnum get(byte type) {
        for (ProtocolEnum value: values()) {
            if (value.type == type) return value;
        }
        throw new RuntimeException("unsupported type:" + type);
    }

    public byte getType() {
        return type;
    }
}
