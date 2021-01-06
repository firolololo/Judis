package com.stellar.judis.server;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/4 16:24
 */
public interface INode {
    void assemble();
    boolean isAssemble();
    boolean start();
    boolean stop();
}
