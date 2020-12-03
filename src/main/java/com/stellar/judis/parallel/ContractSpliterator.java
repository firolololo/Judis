package com.stellar.judis.parallel;

import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @author cloudwalk3212
 * @version 1.0
 * @date 2020/11/26 14:12
 */
public class ContractSpliterator implements Spliterator<Contract> {
    private final List<Contract> contracts;
    private int from;
    private int to;

    public ContractSpliterator(List<Contract> contracts) {
        this(contracts, 0, contracts.size());
    }

    private ContractSpliterator(List<Contract> contracts, int from, int to) {
        this.contracts = contracts;
        this.from = from;
        this.to = to;
    }

    private int size() {
        return to - from;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Contract> action) {
        if (size() > 0) {
            action.accept(contracts.get(from));
            from++;
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<Contract> trySplit() {
        if (size() == 1) {
            return null;
        }
        Spliterator<Contract> part = new ContractSpliterator(contracts, from + size() / 2, to);
        this.to -= size() / 2;
        return part;
    }

    @Override
    public long estimateSize() {
        return 0;
    }

    @Override
    public int characteristics() {
        return SIZED;
    }

    public static void main(String[] args) {
        int cap = 966;
        System.out.println(Integer.toBinaryString(cap));
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        System.out.println(n);
    }
}
