package com.stellar.judis.meta;

import com.stellar.judis.rpc.CommandType;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/12 16:34
 */
public class JudisString extends JudisElement implements IJudisElementType {
    private String value;
    public JudisString(String value) {
        setType(CommandType.STRING);
        setTypeImpl(this);
        this.value = value;
    }

    @Override
    public String serialize() {
        return value;
    }

    @Override
    public JudisElement deserialize(String data) {
        return new JudisString(data);
    }
}
