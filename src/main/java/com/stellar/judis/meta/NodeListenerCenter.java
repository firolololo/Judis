package com.stellar.judis.meta;

import com.stellar.judis.server.Node;

import java.util.LinkedList;
import java.util.List;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/6 17:18
 */
public class NodeListenerCenter {
    private List<INodeListener> listenerList;

    public NodeListenerCenter() {
        this.listenerList = new LinkedList<>();
    }

    public void register(INodeListener listener) {
        this.listenerList.add(listener);
    }

    public void triggerPrepareEvent(NodeContext context) {
        for (INodeListener listener: listenerList) {
            listener.dispatch(context, INodeListener.Phase.PREPARE);
        }
    }

    public void triggerStartEvent(NodeContext context) {
        for (INodeListener listener: listenerList) {
            listener.dispatch(context, INodeListener.Phase.START);
        }
    }

    public void triggerSuccessEvent(NodeContext context) {
        for (INodeListener listener: listenerList) {
            listener.dispatch(context, INodeListener.Phase.SUCCESS);
        }
    }

    public void triggerFailEvent(NodeContext context) {
        for (INodeListener listener: listenerList) {
            listener.dispatch(context, INodeListener.Phase.FAIL);
        }
    }
}
