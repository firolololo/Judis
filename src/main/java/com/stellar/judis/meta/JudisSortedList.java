package com.stellar.judis.meta;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/14 17:13
 */
public class JudisSortedList extends JudisElement implements IJudisElementType {
    private SkipList<String> skipList;

    public JudisSortedList() {
        skipList = new SkipList<>();
    }

    @Override
    public String serialize() {
        return null;
    }

    @Override
    public JudisElement deserialize(String data) {
        return null;
    }
}
