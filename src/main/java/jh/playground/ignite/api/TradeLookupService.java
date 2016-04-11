package jh.playground.ignite.api;


import jh.playground.ignite.domain.Trade;

public interface TradeLookupService {
    Trade lookup(int tradePkey);
}
