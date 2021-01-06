package com.stellar.judis.server;

import com.stellar.judis.server.Master;
import com.stellar.judis.server.Node;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/4 14:53
 */
public class Servant extends Node {
    private Master master;
    Servant(Master master, String address, int port) {
        super(address, port);
        this.master = master;
    }

    @Override
    public void assemble() {

    }

    @Override
    public boolean isAssemble() {
        return true;
    }
}
