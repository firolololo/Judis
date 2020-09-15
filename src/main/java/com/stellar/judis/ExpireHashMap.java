package com.stellar.judis;

import java.util.concurrent.ConcurrentMap;


/**
 * @author cloudwalk3212
 * @version 1.0
 * @date 2020/8/21 15:45
 */
public class ExpireHashMap<K, V> {
    private ConcurrentMap<K, ExpireV<V>> concurrentMap;

}
