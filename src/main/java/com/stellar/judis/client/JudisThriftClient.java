package com.stellar.judis.client;

import com.alibaba.fastjson.JSON;
import com.stellar.judis.rpc.Command;
import com.stellar.judis.rpc.GetResponse;
import com.stellar.judis.rpc.Heartbeat;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * @author firo
 * @version 1.0
 * @date 2020/12/15 19:25
 */
public class JudisThriftClient {

    public static void sendCommand(Command.Client client) throws TException {
        GetResponse response = client.getValue("key");
        System.out.println(JSON.toJSONString(response));
    }

    public static void sendPing(Heartbeat.Client client) throws TException {
        client.ping();
        System.out.println("ping ok");
    }

    public static void doClient() {
        try {
            TTransport transport = new TFramedTransport(new TSocket("127.0.0.1", 8222));
            TProtocol protocol = new TMultiplexedProtocol(new TCompactProtocol(transport), "Heartbeat");
            Heartbeat.Client client = new Heartbeat.Client(protocol);
            transport.open();
            sendPing(client);
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        doClient();
    }
}
