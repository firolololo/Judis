package com.stellar.judis;

import com.stellar.judis.server.Master;
import com.stellar.judis.server.sentinel.Sentinel;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/6 18:03
 */
public class SentinelMain {
    public static void main(String[] args) {
        Sentinel sentinel = new Sentinel("127.0.0.1", 24889);
        sentinel.run();
        Master master = new Master("127.0.0.1", 8766);
        sentinel.addMaster(master);
    }
}
