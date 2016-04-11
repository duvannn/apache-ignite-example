package jh.playground.ignite.task;


import jh.playground.ignite.domain.Trade;

import java.util.Collection;
import java.util.Date;

public class ValuationsRequest {
    public Collection<Trade> trades;
    public Date valuationDate;

    public ValuationsRequest(Collection<Trade> trades, Date valuationDate) {
        this.trades = trades;
        this.valuationDate = valuationDate;
    }

    @Override
    public String toString() {
        return "ValuationsRequest{" +
                "trades=" + trades +
                ", valuationDate=" + valuationDate +
                '}';
    }
}
