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

    public JudisCoreOperation(PersistAdaptor adaptor, boolean isMaster) {
        this.adaptor = adaptor;
        this.isMaster = isMaster;
    }

    private Cache cache = new Cache();

    @Override
    public JudisElement get(String key) {
        return cache.get(key);
    }

    @Override
    public JudisElement put(String key, JudisElement value) {
        if (isMaster) {
//            adaptor.parse(JudisOperationBean.PUT, key, value);
            return cache.put(key, value);
        }
        return null;
    }

    @Override
    public JudisElement put(String key, JudisElement value, long times, TimeUnit unit) {
        if (isMaster) {
            LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(unit.toMinutes(times));
//            adaptor.parse(JudisOperationBean.PUT, key, value, localDateTime.toString());
            return put(key, value, localDateTime);
        }
        return null;
    }

    @Override
    public JudisElement put(String key, JudisElement value, LocalDateTime localDateTime) {
        if (isMaster) {
//            adaptor.parse(JudisOperationBean.PUT, key, value, localDateTime.toString());
            return cache.put(key, value, localDateTime);
        }
        return null;
    }

    @Override
    public JudisElement delete(String key) {
        if (isMaster) {
//            adaptor.parse(JudisOperationBean.DELETE, key);
            return cache.remove(key);
        }
        return null;
    }
}
