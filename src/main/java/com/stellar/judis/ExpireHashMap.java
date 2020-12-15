package com.stellar.judis;

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

    public ExpireHashMap() {
        this.expireMap = new ConcurrentHashMap<>();
    }

    public V putExpire(K k, V v, long duration, TemporalUnit temporalUnit) {
        checkNull(k);
        checkNull(v);
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime.plus(duration, temporalUnit);
        ExpireV<V> expireV = expireMap.put(k, new ExpireV<V>(v, localDateTime));
        return Optional.ofNullable(expireV).map(ExpireV::getValue).orElse(null);
    }

    public V put(K k, V v) {
        checkNull(k);
        checkNull(v);
        ExpireV<V> expireV = expireMap.put(k, new ExpireV<V>(v, null));
        return Optional.ofNullable(expireV).map(ExpireV::getValue).orElse(null);
    }

    public V get(K k) {
        checkNull(k);
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

    private void checkNull(Object obj) {
        if (obj == null) throw new RuntimeException("Null key or value is not permitted");
    }
}
