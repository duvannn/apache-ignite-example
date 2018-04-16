package de.jhoffmann.domain;

public class Trade {
    private int tradePkey;
    public String tradeId;
    private String book;
    private String account;
    public String type;
    private String tradeCurrency;

    public Trade(int tradePkey, String tradeId, String book, String account, String type, String tradeCurrency) {
        this.tradePkey = tradePkey;
        this.tradeId = tradeId;
        this.book = book;
        this.account = account;
        this.type = type;
        this.tradeCurrency = tradeCurrency;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "account='" + account + '\'' +
                ", tradePkey=" + tradePkey +
                ", tradeId='" + tradeId + '\'' +
                ", book='" + book + '\'' +
                ", type='" + type + '\'' +
                ", tradeCurrency='" + tradeCurrency + '\'' +
                '}';
    }
}
