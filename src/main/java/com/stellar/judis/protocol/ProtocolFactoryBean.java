package com.stellar.judis.protocol;

/**
 * @author cloudwalk3212
 * @version 1.0
 * @date 2020/12/3 14:52
 */
public enum ProtocolFactoryBean {
    JUDIS((byte)1, new judisProtocol());
    private byte type;
    private Protocol protocol;

    ProtocolFactoryBean(byte type, Protocol protocal) {
        this.type = type;
        this.protocol = protocal;
    }

    public static Protocol getProtocol(byte type) {
        for (ProtocolFactoryBean bean: values()) {
            if (bean.type == type) {
                return bean.protocol;
            }
        }
        throw new RuntimeException("no unsupported protocol");
    }
}
