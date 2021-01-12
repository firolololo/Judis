package com.stellar.judis.server;

import com.stellar.judis.handler.*;
import com.stellar.judis.meta.JudisElement;
import com.stellar.judis.rpc.ClientToServer;
import com.stellar.judis.rpc.Heartbeat;
import com.stellar.judis.rpc.SentinelOtherNode;
import com.stellar.judis.server.core.CoreOperation;
import com.stellar.judis.server.core.JudisCoreOperation;
import com.stellar.judis.server.persist.AofAdaptor;
import com.stellar.judis.server.task.MasterUpdateTask;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/4 14:52
 */
public class Master extends Node {
    private static final InternalLogger LOG = InternalLoggerFactory.getInstance(Master.class);
    private List<Servant> servantList;
    private transient CoreOperation<String, JudisElement> coreOperation;
    private volatile transient AtomicBoolean completed = new AtomicBoolean(false);

    public Master(String address, int port, CoreOperation<String, JudisElement> coreOperation) {
        super(address, port);
        servantList = new LinkedList<>();
        this.coreOperation = coreOperation;
    }

    public Master(String address, int port) {
        super(address, port);
        servantList = new LinkedList<>();
        // init opt
        coreOperation = new JudisCoreOperation(new AofAdaptor(), true);
    }

    public void run() {
        assemble();
        start();
        configTask();
    }

    public void configTask() {
        if (this.listenChannel != null) {
            MasterUpdateTask updateTask = new MasterUpdateTask();
            this.listenChannel.eventLoop().scheduleAtFixedRate(updateTask, 1L, 1L, TimeUnit.SECONDS);
        }
    }

    @Override
    public void assemble() {
        if (completed.compareAndSet(false, true)) {
            try {
//                processor.registerProcessor("Heartbeat", new Heartbeat.Processor<>(new HeartbeatHandler()));
                processor.registerProcessor("Sentinel", new SentinelOtherNode.Processor<>(new SentinelOtherNodeHandler(this)));
            } catch (Exception e) {
                completed.compareAndSet(true, false);
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isAssemble() {
        return completed.get();
    }

    public boolean addServant(Servant servant) {
        return this.servantList.add(servant);
    }
}
