package com.stellar.judis.meta;

import java.util.*;

/**
 * @author cloudwalk3212
 * @version 1.0
 * @date 2020/9/6 15:39
 */
public class AbstractSortedSet<E> extends AbstractSet<E> implements SortedSet<E> {

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Comparator<? super E> comparator() {
        return null;
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        return null;
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        return null;
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        return null;
    }

    @Override
    public E first() {
        return null;
    }

    @Override
    public E last() {
        return null;
    }
}
