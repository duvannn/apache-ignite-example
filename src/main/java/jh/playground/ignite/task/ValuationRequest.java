package jh.playground.ignite.task;


import jh.playground.ignite.domain.Trade;

import java.util.Date;

public class ValuationRequest {
    public Trade trade;
    public Date valuationDate;

    public ValuationRequest(Trade trade, Date valuationDate) {
        this.trade = trade;
        this.valuationDate = valuationDate;
    }

    @Override
    public String toString() {
        return "ValuationRequest{" +
                "valuationDate=" + valuationDate +
                ", trade=" + trade +
                '}';
    }
}
