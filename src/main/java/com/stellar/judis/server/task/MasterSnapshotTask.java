package com.stellar.judis.server.task;

import com.stellar.judis.meta.JudisElement;
import com.stellar.judis.server.core.CoreOperation;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/20 16:48
 */
public class MasterSnapshotTask implements Runnable {
    private static final InternalLogger LOG = InternalLoggerFactory.getInstance(MasterSnapshotTask.class);
    private String masterId;
    private CoreOperation<String, JudisElement> coreOperation;
    public MasterSnapshotTask(String masterId, CoreOperation<String, JudisElement> coreOperation) {
        this.masterId = masterId;
        this.coreOperation = coreOperation;
    }
    @Override
    public void run() {
        try {
            coreOperation.snapshot(null);
            String threadInfo = Thread.currentThread().getName() + "-" + Thread.currentThread().getId();
            LOG.info("Master {} snapshot success, Thread info:{}", masterId, threadInfo);
        } catch (Exception e) {
            LOG.error("Master {} snapshot failed", masterId, e);
        }
    }
}
