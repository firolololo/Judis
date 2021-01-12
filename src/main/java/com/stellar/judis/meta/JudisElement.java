package com.stellar.judis.meta;

import com.stellar.judis.rpc.CommandType;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/12 16:15
 */
public class JudisElement {
    private CommandType type;
    private IJudisElementType typeImpl;

    public CommandType getType() {
        return type;
    }

    public void setType(CommandType type) {
        this.type = type;
    }

    public IJudisElementType getTypeImpl() {
        return typeImpl;
    }

    public void setTypeImpl(IJudisElementType typeImpl) {
        this.typeImpl = typeImpl;
    }
}
