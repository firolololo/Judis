package com.stellar.judis.server.persist;

import com.stellar.judis.meta.Cache;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author firo
 * @version 1.0
 * @date 2020/12/30 16:54
 */
public class AofAdaptor<K, V> implements PersistAdaptor<K, V> {
    private List<String> buffer = new LinkedList<>();
    private static final int BUFFER_MAX_CAPACITY = 2048;

    @Override
    public Cache<K, V> load() {
        return null;
    }

    @Override
    public void snapshot(Cache<K, V> cache) {

    }

    @Override
    public void parse(String operation) {
        buffer.add(operation);
    }
}
