package com.stellar.judis;

import com.sun.istack.internal.NotNull;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author cloudwalk3212
 * @version 1.0
 * @date 2020/8/21 15:45
 */
public class ExpireHashMap<K, V> {
    private ConcurrentHashMap<K, ExpireV<V>> expireMap;

    public void ExpireHashMap() {
        this.expireMap = new ConcurrentHashMap<>();
    }

    public V putExpire(@NotNull() K k, @NotNull() V v, long duration, @NotNull()TemporalUnit temporalUnit) {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime.plus(duration, temporalUnit);
        ExpireV<V> expireV = expireMap.put(k, new ExpireV<V>(v, localDateTime));
        return Optional.ofNullable(expireV).map(ExpireV::getValue).orElse(null);
    }

    public V put(@NotNull() K k, @NotNull() V v) {
        ExpireV<V> expireV = expireMap.put(k, new ExpireV<V>(v, null));
        return Optional.ofNullable(expireV).map(ExpireV::getValue).orElse(null);
    }

    public V get(@NotNull() K k) {
        ExpireV<V> expireV = expireMap.get(k);
        if (null == expireV) {
            return null;
        }
        if (null == expireV.getLocalDateTime()) {
            return expireV.getValue();
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        if (localDateTime.isAfter(expireV.getLocalDateTime())) {
            return null;
        }
        return expireV.getValue();
    }
}
