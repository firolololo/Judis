package com.stellar.judis.server.sentinel;

import com.stellar.judis.meta.NodeListenerCenter;
import com.stellar.judis.server.Master;
import com.stellar.judis.server.Node;
import com.stellar.judis.server.task.SentinelPingTask;
import io.netty.util.HashedWheelTimer;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

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
    private Map<String, TTransport> socketMap;
    private Map<String, List<ScheduledFuture<?>>> taskMap;

    public Sentinel(String address, int port) {
        super(address, port);
        // 记录master信息
        masterMap = new HashMap<>();
        socketMap = new HashMap<>();
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
        socketMap.remove(masterId);
        if (taskMap.containsKey(masterId))
            for (ScheduledFuture<?> future: taskMap.get(masterId))
                future.cancel(false);
    }

    public void updateMasterInfo(Master master) {
        if (!masterMap.containsKey(master.getId()))
            addMaster(master);
        else
            masterMap.put(master.getId(), master);
    }

    public boolean addMaster(Master master) {
        try {
            if (masterMap.containsKey(master.getId()))
                return false;
            masterMap.put(master.getId(), master);
            TTransport transport = new TFramedTransport(new TSocket(master.getAddress(), master.getPort()));
            socketMap.put(master.getId(), transport);
            if (taskMap.containsKey(master.getId()))
                for (ScheduledFuture<?> future: taskMap.get(master.getId()))
                    future.cancel(true);
            else
                registerTask(master);

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void registerTask(Master master) {
        if (listenChannel != null && socketMap.containsKey(master.getId())) {
            TTransport transport = socketMap.get(master.getId());
            SentinelPingTask pingTask = new SentinelPingTask(this, master.getId(), transport);
            ScheduledFuture<?> scheduledFuture = this.listenChannel.eventLoop().scheduleAtFixedRate(pingTask, 1L, 1L, TimeUnit.SECONDS);
            if (!taskMap.containsKey(master.getId()))
                taskMap.put(master.getId(), new LinkedList<>());
            taskMap.get(master.getId()).add(scheduledFuture);
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
