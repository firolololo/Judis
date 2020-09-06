package com.stellar.judis;

/**
 * @author cloudwalk3212
 * @version 1.0
 * @date 2020/8/21 15:25
 */
public interface ExpireMeta<K, V> {
    void put(K key, V value, long expires);
    V get(K key);
}
