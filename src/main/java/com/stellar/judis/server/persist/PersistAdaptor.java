package com.stellar.judis.server.persist;

import com.stellar.judis.meta.Cache;

/**
 * @author firo
 * @version 1.0
 * @date 2020/12/30 16:53
 */
public interface PersistAdaptor {
    /**
     * 将持久化文件的内容加载到Cache
     * @return
     */
    Cache load();

    /**
     * 将当前Cache的内容进行快照，写入持久化文件
     * 快照点可以生产索引
     * @param cache
     */
    void snapshot(Cache cache);

    /**
     * 序列化Judis操作，并且写入缓存
     * @param operationBean
     * @param args
     */
    void parse(JudisOperationBean operationBean, String... args);

    /**
     * 将缓存写入持久化文件
     */
    void update();
}
