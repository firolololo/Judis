package com.stellar.judis.server;

import com.stellar.judis.rpc.Directive;
import com.stellar.judis.rpc.Instruction;
import com.stellar.judis.rpc.SentinelOtherNode;
import io.netty.util.HashedWheelTimer;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/4 14:48
 */
public class Sentinel extends Node {
    private static final InternalLogger LOG = InternalLoggerFactory.getInstance(Sentinel.class);
    private List<Master> masterList;
    private Map<Master, TTransport> socketMap;
    private HashedWheelTimer timer;

    public Sentinel(String address, int port) {
        super(address, port);
        masterList = new LinkedList<>();
        socketMap = new HashMap<>();
        timer = new HashedWheelTimer();
    }

    public boolean addMaster(Master master) {
        try {
            masterList.add(master);
            TTransport transport = new TFramedTransport(new TSocket(master.getAddress(), master.getPort()));
            socketMap.put(master, transport);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean assemble() {
        return true;
    }

    private void updateMaster() {
        for (Map.Entry<Master, TTransport> entry: socketMap.entrySet()) {
            try {
                TTransport transport = entry.getValue();
                TProtocol protocol = new TMultiplexedProtocol(new TCompactProtocol(transport), "Sentinel");
                SentinelOtherNode.Client client = new SentinelOtherNode.Client(protocol);
                transport.open();
                Instruction instruction = new Instruction();
                instruction.setDirective(Directive.UPDATE);
                client.instruction(instruction);
                transport.close();
            } catch (Exception e) {
                LOG.error("Master node address:{} update failed", entry.getKey().address + ":" + entry.getKey().port, e);
            }
        }
    }

    public void loadTimerTask() {
        timer.newTimeout(timeout -> updateMaster(), 1, TimeUnit.SECONDS);
    }


}
