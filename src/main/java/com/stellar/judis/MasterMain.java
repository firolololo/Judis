package com.stellar.judis;

import com.stellar.judis.meta.JudisElement;
import com.stellar.judis.meta.JudisString;
import com.stellar.judis.server.Master;
import com.stellar.judis.server.core.CoreOperation;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/4 16:57
 */
public class MasterMain {
    public static void main(String[] args) {
        Master master = new Master("127.0.0.1", 8768);
        master.run();
//        CoreOperation<String, JudisElement> coreOperation = master.getCoreOperation();
//        for (int i = 0; i < Integer.MAX_VALUE; i++) {
//            coreOperation.put(String.valueOf(i), new JudisString(String.valueOf(i)));
//        }
    }
}
