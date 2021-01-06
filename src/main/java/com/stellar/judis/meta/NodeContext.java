package com.stellar.judis.meta;

import com.stellar.judis.server.Master;
import com.stellar.judis.server.Servant;
import com.stellar.judis.server.sentinel.Sentinel;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/6 17:07
 */
public class NodeContext {
    private Sentinel sentinel;
    private Master master;
    private Servant servant;

    public Sentinel getSentinel() {
        return sentinel;
    }

    public void setSentinel(Sentinel sentinel) {
        this.sentinel = sentinel;
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    public Servant getServant() {
        return servant;
    }

    public void setServant(Servant servant) {
        this.servant = servant;
    }
}
