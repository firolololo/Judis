package com.stellar.judis.meta;

import com.stellar.judis.rpc.CommandType;

import java.util.BitSet;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/18 11:28
 */
public class JudisBitSet extends JudisElement implements IJudisElementType {
    private BitSet bitSet;

    public JudisBitSet() {
        setType(CommandType.BITSET);
        setTypeImpl(this);
        this.bitSet = new BitSet();
    }

    public JudisBitSet(BitSet bitSet) {
        setType(CommandType.BITSET);
        setTypeImpl(this);
        this.bitSet = bitSet;
    }

    public BitSet getBitSet() {
        return bitSet;
    }

    public void setBitSet(BitSet bitSet) {
        this.bitSet = bitSet;
    }

    @Override
    public String serialize() {
        return new String(this.bitSet.toByteArray());
    }

    @Override
    public JudisElement deserialize(String data) {
        return new JudisBitSet(BitSet.valueOf(data.getBytes()));
    }
}
