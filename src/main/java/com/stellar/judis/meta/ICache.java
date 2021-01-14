package com.stellar.judis.meta;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author firo
 * @version 1.0
 * @date 2020/12/30 15:42
 */
public interface ICache<K, V> {
    /**
     * 获取值
     * @param key
     * @return
     */
    V get(K key);

    /**
     * 检查键是否存在
     * @param key
     * @return
     */
    boolean containsKey(K key);

    /**
     * 设置键值对
     * @param key
     * @param value
     * @return
     */
    V put(K key, V value);

    /**
     * 设置具有过期时间的键值对
     * @param key
     * @param value
     * @param times
     * @param unit
     * @return
     */
    V put(K key, V value, long times, TimeUnit unit);

    /**
     * 设置具有过期时间的键值对
     * @param key
     * @param value
     * @param localDateTime
     * @return
     */
    V put(K key, V value, LocalDateTime localDateTime);

    /**
     * 删除键值对
     * @param key
     * @return
     */
    V remove(K key);

    /**
     * 获取缓存中键的过期时间点
     * @param key
     * @return
     */
    LocalDateTime getExpireTime(K key);

}
