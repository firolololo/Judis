package com.stellar.judis.client;

import com.stellar.judis.rpc.ClientStringCommand;
import com.stellar.judis.rpc.CommandResponse;
import com.stellar.judis.util.JsonUtil;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/18 17:11
 */
public class JudisClient {
    public static void setStringPair(String key, String value) {
        try {
            TTransport transport = new TFramedTransport(new TSocket("127.0.0.1", 8768));
            TProtocol protocol = new TMultiplexedProtocol(new TCompactProtocol(transport), "StringCommand");
            ClientStringCommand.Client client = new ClientStringCommand.Client(protocol);
            transport.open();
            CommandResponse response = client.setString(key, value, -1, false);
            System.out.println(JsonUtil.jsonToString(response));
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            String key = String.valueOf(i);
            String value = String.valueOf(i);
            new Thread(() -> setStringPair(key, value)).start();
        }
    }
}
