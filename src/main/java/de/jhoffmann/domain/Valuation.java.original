package jh.playground.ignite.domain;

import java.util.Date;

public class Valuation {
    private int valuationPkey;
    private Date valuationDate;
    private double pv;
    private double pvLocal;
    private final String tradeId;


    public Valuation(int valuationPkey, Date valuationDate, double pv, double pvLocal, String tradeId) {
        this.valuationPkey = valuationPkey;
        this.valuationDate = valuationDate;
        this.pv = pv;
        this.pvLocal = pvLocal;
        this.tradeId = tradeId;
    }

    @Override
    public String toString() {
        return "Valuation{" +
                "valuationDate=" + valuationDate +
                ", valuationPkey=" + valuationPkey +
                ", tradeId=" + tradeId +
                ", pv=" + pv +
                ", pvLocal=" + pvLocal +
                '}';
    }
}
