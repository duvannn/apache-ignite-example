package de.jhoffmann.computetask;


import de.jhoffmann.domain.Trade;

import java.util.Collection;
import java.util.Date;

public class MultiTradesRequest {
    Collection<Trade> trades;
    Date valuationDate;

    public MultiTradesRequest(Collection<Trade> trades, Date valuationDate) {
        this.trades = trades;
        this.valuationDate = valuationDate;
    }

    @Override
    public String toString() {
        return "MultiTradesRequest{" +
                "trades=" + trades +
                ", valuationDate=" + valuationDate +
                '}';
    }
}
