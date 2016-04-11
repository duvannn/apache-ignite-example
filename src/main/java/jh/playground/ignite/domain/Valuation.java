package jh.playground.ignite.domain;

import java.util.Date;

public class Valuation {
    private int valuationPkey;
    private Date valuationDate;
    private double pv;
    private double pvLocal;


    public Valuation(int valuationPkey, Date valuationDate, double pv, double pvLocal) {
        this.valuationPkey = valuationPkey;
        this.valuationDate = valuationDate;
        this.pv = pv;
        this.pvLocal = pvLocal;
    }

    @Override
    public String toString() {
        return "Valuation{" +
                "valuationDate=" + valuationDate +
                ", valuationPkey=" + valuationPkey +
                ", pv=" + pv +
                ", pvLocal=" + pvLocal +
                '}';
    }
}
