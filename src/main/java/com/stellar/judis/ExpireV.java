package com.stellar.judis;

import java.time.LocalDateTime;

/**
 * Created by wanglecheng on 2020/9/15.
 */
public class ExpireV<T> {
    private T value;
    private LocalDateTime localDateTime;

    public ExpireV(T t) {
        this.value = t;
    }
}
