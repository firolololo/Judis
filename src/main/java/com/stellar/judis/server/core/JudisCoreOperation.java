package com.stellar.judis.server.core;

import com.stellar.judis.meta.Cache;
import com.stellar.judis.meta.JudisElement;
import com.stellar.judis.server.persist.JudisOperationBean;
import com.stellar.judis.server.persist.PersistAdaptor;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author firo
 * @version 1.0
 * @date 2020/12/30 17:09
 */
public class JudisCoreOperation implements CoreOperation<String, JudisElement> {
    private PersistAdaptor adaptor;
    private boolean isMaster;
    private Cache cache;

    public JudisCoreOperation(PersistAdaptor adaptor, boolean isMaster) {
        this.adaptor = adaptor;
        this.isMaster = isMaster;
        this.cache = this.load();
    }

    @Override
    public JudisElement get(String key) {
        return cache.get(key);
    }

    @Override
    public boolean containsKey(String s) {
        return cache.containsKey(s);
    }

    @Override
    public JudisElement put(String key, JudisElement value) {
        if (isMaster) {
            String className = value.getClass().getName();
            adaptor.parse(JudisOperationBean.PUT, className, value, key);
            return cache.put(key, value);
        }
        return null;
    }

    @Override
    public JudisElement put(String key, JudisElement value, long times, TimeUnit unit) {
        if (isMaster) {
            LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(unit.toSeconds(times));
            return put(key, value, localDateTime);
        }
        return null;
    }

    @Override
    public JudisElement put(String key, JudisElement value, LocalDateTime localDateTime) {
        if (isMaster) {
            String className = value.getClass().getName();
            adaptor.parse(JudisOperationBean.PUT, className, value, key, localDateTime.toString());
            return cache.put(key, value, localDateTime);
        }
        return null;
    }

    @Override
    public JudisElement delete(String key) {
        if (isMaster) {
            adaptor.parse(JudisOperationBean.DELETE, "", null, key);
            return cache.remove(key);
        }
        return null;
    }

    @Override
    public Cache load() {
        this.cache = this.adaptor.load();
        return this.cache;
    }

    @Override
    public void snapshot(Cache cache) {
        this.adaptor.snapshot(this.cache);
    }

    @Override
    public void parse(JudisOperationBean operationBean, String className, JudisElement element, String... args) {
        this.adaptor.parse(operationBean, className, element, args);
    }

    @Override
    public int update() {
        return this.adaptor.update();
    }
}
