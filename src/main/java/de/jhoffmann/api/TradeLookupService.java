package de.jhoffmann.api;


import de.jhoffmann.domain.Trade;

public interface TradeLookupService {
    Trade lookup(int tradePkey);
}
