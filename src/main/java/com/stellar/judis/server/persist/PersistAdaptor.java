package com.stellar.judis.server.persist;

import com.stellar.judis.meta.Cache;

/**
 * @author firo
 * @version 1.0
 * @date 2020/12/30 16:53
 */
public interface PersistAdaptor<K, V> {
    Cache<K, V> load();
    void snapshot(Cache<K, V> cache);
    void parse(String operation);
}
