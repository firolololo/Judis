package com.stellar.judis.server.core;

import com.stellar.judis.server.persist.PersistAdaptor;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author firo
 * @version 1.0
 * @date 2020/12/30 16:50
 */
public interface CoreOperation<K, V> extends PersistAdaptor {
    V get(K key);
    boolean containsKey(K k);
    V put(K key, V value);
    V put(K key, V value, long times, TimeUnit unit);
    V put(K key, V value, LocalDateTime localDateTime);
    V delete(K key);
}
