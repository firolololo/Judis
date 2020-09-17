package com.stellar.judis;

import java.time.LocalDateTime;

/**
 * Created by wanglecheng on 2020/9/15.
 */
public class ExpireV<T> {
    private T value;
    private LocalDateTime localDateTime;

    public ExpireV(T t, LocalDateTime localDateTime) {
        this.value = t;
        this.localDateTime = localDateTime;
    }

    public T getValue() {
        return value;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }
}
