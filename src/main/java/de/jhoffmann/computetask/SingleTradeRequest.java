package de.jhoffmann.computetask;


import de.jhoffmann.domain.Trade;

import java.util.Date;

public class SingleTradeRequest {
    Trade trade;
    Date valuationDate;

    public SingleTradeRequest(Trade trade, Date valuationDate) {
        this.trade = trade;
        this.valuationDate = valuationDate;
    }

    @Override
    public String toString() {
        return "SingleTradeRequest{" +
                "valuationDate=" + valuationDate +
                ", trade=" + trade +
                '}';
    }
}
