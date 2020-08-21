package com.stellar.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author cloudwalk3212
 * @version 1.0
 * @date 2020/8/21 15:45
 */
public class ExpireHashMap<K, V> implements ExpireMeta<K, V> {
    private ConcurrentHashMap<K, V> vMap;
    private ConcurrentHashMap<K, AtomicReference<V>> kExpireMap;

    public ExpireHashMap() {
        this.vMap = new ConcurrentHashMap<K, V>();
        this.kExpireMap = new ConcurrentHashMap<K, AtomicReference<V>>();
    }

    public void put(K key, V value, long expires) {

    }

    public V get(K key) {
        return null;
    }
}
