package com.stellar.judis.server.sentinel;

import com.stellar.judis.meta.NodeListenerCenter;
import com.stellar.judis.server.Master;
import com.stellar.judis.server.Node;
import com.stellar.judis.server.task.SentinelPingTask;
import com.stellar.judis.server.task.SentinelSnapshotTask;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/4 14:48
 */
public class Sentinel extends Node {
    private static final InternalLogger LOG = InternalLoggerFactory.getInstance(Sentinel.class);
    private NodeListenerCenter center;
    private Map<String, Master> masterMap;
    private Map<String, List<ScheduledFuture<?>>> taskMap;

    public Sentinel(String address, int port) {
        super(address, port);
        // 记录master信息
        masterMap = new HashMap<>();
        taskMap = new HashMap<>();
        // 事件处理中心
        center = new NodeListenerCenter();
        center.register(new SentinelMasterNodeListener());
    }

    public NodeListenerCenter getEventCenter() { return center; }

    public Master getMaster(String masterId) { return masterMap.get(masterId); }

    public void run() {
        start();
    }

    public void clearMasterInfo(String masterId) {
        masterMap.remove(masterId);
        if (taskMap.containsKey(masterId)) {
            for (ScheduledFuture<?> future: taskMap.get(masterId)){
                future.cancel(false);
            }
        }
        taskMap.remove(masterId);
    }

    public void updateMasterInfo(Master master) {
        if (!masterMap.containsKey(master.getId()))
            addMaster(master);
        else
            masterMap.put(master.getId(), master);
    }

    public synchronized boolean addMaster(Master master) {
        try {
            if (masterMap.containsKey(master.getId()))
                return false;
            masterMap.put(master.getId(), master);
            if (taskMap.containsKey(master.getId())) {
                for (ScheduledFuture<?> future: taskMap.get(master.getId())) {
                    future.cancel(true);
                }
            }
            taskMap.remove(master.getId());
            registerTask(master);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void registerTask(Master master) {
        if (listenChannel != null) {
            SentinelPingTask pingTask = new SentinelPingTask(this, master.getId());
            ScheduledFuture<?> pingFuture = this.listenChannel.eventLoop().scheduleAtFixedRate(pingTask, 1L, 1L, TimeUnit.SECONDS);
            SentinelSnapshotTask snapshotTask = new SentinelSnapshotTask(this, master.getId());
            ScheduledFuture<?> snapshotFuture = this.listenChannel.eventLoop().scheduleAtFixedRate(snapshotTask, 60L, 60L, TimeUnit.SECONDS);
            if (!taskMap.containsKey(master.getId()))
                taskMap.put(master.getId(), new LinkedList<>());
            taskMap.get(master.getId()).add(pingFuture);
            taskMap.get(master.getId()).add(snapshotFuture);
        }
    }

    @Override
    public void assemble() {

    }

    @Override
    public boolean isAssemble() {
        return true;
    }
}
