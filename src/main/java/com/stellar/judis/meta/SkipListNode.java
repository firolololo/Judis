package com.stellar.judis.meta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cloudwalk3212
 * @version 1.0
 * @date 2020/9/6 15:26
 */
public class SkipListNode<E> {
    public List<SkipListNode<E>> nextNodes;
    private E value;

    public E getValue() {
        return value;
    }

    public SkipListNode(E value) {
        this.value = value;
        nextNodes = new ArrayList<>();
    }

    public int level() {
        return nextNodes.size() - 1;
    }

    public String toString() {
        return String.valueOf(value);
    }


}
