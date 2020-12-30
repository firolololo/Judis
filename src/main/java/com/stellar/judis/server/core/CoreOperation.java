package com.stellar.judis.server.core;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author firo
 * @version 1.0
 * @date 2020/12/30 16:50
 */
public interface CoreOperation<K, V> {
    V get(K key);
    V put(K key, V value);
    V put(K key, V value, long times, TimeUnit unit);
    V put(K key, V value, LocalDateTime localDateTime);
    V delete(K key);
}
