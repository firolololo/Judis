package com.stellar.judis;

import com.stellar.judis.server.Master;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/4 16:57
 */
public class MasterMain {
    public static void main(String[] args) {
        Master master = new Master("127.0.0.1", 8766);
        master.run();
    }
}
