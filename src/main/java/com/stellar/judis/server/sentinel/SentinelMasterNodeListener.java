package com.stellar.judis.server.sentinel;

import com.stellar.judis.meta.INodeListener;
import com.stellar.judis.meta.NodeContext;
import com.stellar.judis.server.Master;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/6 16:15
 */
public class SentinelMasterNodeListener implements INodeListener {
    private static final InternalLogger LOG = InternalLoggerFactory.getInstance(SentinelMasterNodeListener.class);

    @Override
    public void onPrepare(NodeContext context) {

    }

    @Override
    public void onStart(NodeContext context) {

    }

    @Override
    public void onSuccess(NodeContext context) {
        Sentinel sentinel = context.getSentinel();
        Master master = context.getMaster();
        if (sentinel == null || master == null) return;
        sentinel.updateMasterInfo(master);
        LOG.info("Update master info address:{}, port:{}", master.getAddress(), master.getPort());
    }

    @Override
    public void onFail(NodeContext context) {
        Sentinel sentinel = context.getSentinel();
        Master master = context.getMaster();
        if (sentinel == null || master == null) return;
        sentinel.clearMasterInfo(master.getId());
        sentinel.updateMasterInfo(master);
        LOG.info("Clear master info address:{}, port:{}", master.getAddress(), master.getPort());
        // TODO 重新选举master 如果没有servant 则sentinel自己成为master
    }
}
