package com.stellar.judis.meta;

import java.util.Iterator;
import java.util.Optional;

/**
 * @author cloudwalk3212
 * @version 1.0
 * @date 2020/9/6 15:42
 */
public class SkipList<E extends Comparable<E>> extends AbstractSortedSet<E> {
    private SkipListNode<E> head;
    private int maxLevel;
    private int size;

    private static final double PROBABILITY = 0.5;

    public SkipList() {
        size = 0;
        maxLevel = 0;
        head = new SkipListNode<E>(null);
        head.nextNodes.add(null);
    }

    public SkipListNode<E> getHead() {
        return head;
    }

    public boolean add(E e) {
        if(contains(e)) return false;
        size++;
        // random number from 0 to maxLevel+1 (inclusive)
        int level = 0;
        while (Math.random() < PROBABILITY)
            level++;
        while(level > maxLevel) { // should only happen once
            head.nextNodes.add(null);
            maxLevel++;
        }
        SkipListNode<E> newNode = new SkipListNode<>(e);
        SkipListNode<E> current = head;
        do {
            current = findNext(e,current, level);
            newNode.nextNodes.add(0, current.nextNodes.get(level));
            current.nextNodes.set(level, newNode);
        } while (level-- > 0);
        return true;
    }

    public int size() {
        return size;
    }

    public boolean contains(E e) {
        SkipListNode<E> node = find(e);
        return Optional.ofNullable(node).map(SkipListNode::getValue).map(v -> equalTo(e, v)).orElse(false);
    }

    class SkipListIterator<E extends Comparable<E>> implements Iterator<E> {
        SkipList<E> list;
        SkipListNode<E> current;
        public SkipListIterator(SkipList<E> list) {
            this.list = list;
            this.current = list.getHead();
        }

        public boolean hasNext() {
            return current.nextNodes.get(0) != null;
        }

        public E next() {
            current = current.nextNodes.get(0);
            return current.getValue();
        }

        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }
    }

    public Iterator<E> iterator() {
        return new SkipListIterator<E>(this);
    }

    public String toString() {
        String s = "SkipList: ";
        for(Object o : this)
            s += o + ", ";
        return s.substring(0, s.length() - 2);
    }

    private SkipListNode<E> find(E e) {
        return find(e, head, maxLevel);
    }

    private SkipListNode<E> find(E e, SkipListNode<E> current, int level) {
        do {
            current = findNext(e, current, level);
        } while (level-- > 0);
        return current;
    }

    private SkipListNode<E> findNext(E e, SkipListNode<E> current, int level) {
        SkipListNode<E> next = current.nextNodes.get(level);
        while (next != null) {
            E value = next.getValue();
            if (lessThan(e, value)) break;
            current = next;
            next = current.nextNodes.get(level);
        }
        return current;
    }

    private boolean lessThan(E a, E b) {
        return a.compareTo(b) < 0;
    }

    private boolean equalTo(E a, E b) {
        return a.compareTo(b) == 0;
    }

    private boolean greaterThan(E a, E b) {
        return a.compareTo(b) > 0;
    }
}
