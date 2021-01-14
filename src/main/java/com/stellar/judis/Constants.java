package com.stellar.judis;

/**
 * @author firo
 * @version 1.0
 * @date 2020/12/15 19:16
 */
public final class Constants {
    public static final int MAX_CONTENT_LENGTH = 16384000;
    public static final int MAX_BUFF_SIZE = 24 * 1024;
    public static final int PKG_HEAD_LEN = 4;
    public static final int SOCK_BACKLOG = 1024;

    public static final String OPERATION_SUCCESS = "success";
    public static final String GET_ERROR = "nil";

    public static final String VALUE_TYPE_NOT_MATCH = "The type of value not match";
    public static final String KEY_NOT_EXISTED = "Key not existed";
    public static final String KEY_NOT_NULL = "Key not null";
    public static final String VALUE_NOT_NULL = "Value not null";
    public static final String KEY_EXISTED_ALREADY = "Key existed already";
    public static final String VALUE_NOT_LONG = "Value not long";
}
