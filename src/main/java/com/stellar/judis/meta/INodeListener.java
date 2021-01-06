package com.stellar.judis.meta;

/**
 * @author firo
 * @version 1.0
 * @date 2020/12/30 16:02
 */
public interface INodeListener {
    enum Phase {
        PREPARE, START, SUCCESS, FAIL
    }

    /**
     * 节点准备完毕 调用start()之前
     * @param context
     */
    void onPrepare(NodeContext context);

    /**
     * 节点开始监听端口 节点调用start()之后
     * @param context
     */
    void onStart(NodeContext context);

    /**
     * 节点间通信成功
     * @param context
     */
    void onSuccess(NodeContext context);

    /**
     * 节点间通信失败
     * @param context
     */
    void onFail(NodeContext context);

    default void dispatch(NodeContext context, Phase phase) {
        switch (phase) {
            case PREPARE:
                onPrepare(context);
                break;
            case START:
                onStart(context);
                break;
            case SUCCESS:
                onSuccess(context);
                break;
            case FAIL:
                onFail(context);
                break;
        }
    }
}
