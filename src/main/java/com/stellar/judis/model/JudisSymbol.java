package com.stellar.judis.model;

/**
 * Created by wanglecheng on 2020/12/7.
 */
public enum JudisSymbol {
    SEG_SIGN(" ");

    private String symbol;
    JudisSymbol(String symbol) {this.symbol = symbol;}

    public String getSymbol() {return this.symbol;}
}
