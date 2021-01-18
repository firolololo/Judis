package com.stellar.judis.server.task;

import com.stellar.judis.meta.JudisElement;
import com.stellar.judis.server.core.CoreOperation;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/6 14:36
 */
public class MasterUpdateTask implements Runnable {
    private static final InternalLogger LOG = InternalLoggerFactory.getInstance(MasterUpdateTask.class);
    private String masterId;
    private CoreOperation<String, JudisElement> coreOperation;
    public MasterUpdateTask(String masterId, CoreOperation<String, JudisElement> coreOperation) {
        this.masterId = masterId;
        this.coreOperation = coreOperation;
    }

    @Override
    public void run() {
        try {
            int updateItems = this.coreOperation.update();
            LOG.info("Master {} update success, {} items append", masterId, updateItems);
        } catch (Exception e) {
            LOG.error("Master {} update failed", masterId, e);
        }
    }
}
