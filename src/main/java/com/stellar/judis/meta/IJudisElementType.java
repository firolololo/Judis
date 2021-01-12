package com.stellar.judis.meta;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/12 16:21
 */
public interface IJudisElementType {
    String serialize();
    JudisElement deserialize(String data);
}
