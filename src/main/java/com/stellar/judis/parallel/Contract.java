package com.stellar.judis.parallel;

import java.time.LocalDate;

/**
 * @author cloudwalk3212
 * @version 1.0
 * @date 2020/11/26 13:56
 */
public class Contract {
    private final State state;
    private final int priceIncent;
    private final LocalDate date;

    public Contract(State state, int priceIncent, LocalDate date) {
        this.state = state;
        this.priceIncent = priceIncent;
        this.date = date;
    }

    public State getState() { return state; }

    public int getPriceIncent() { return priceIncent; }

    public LocalDate getDate() { return date; }
}
