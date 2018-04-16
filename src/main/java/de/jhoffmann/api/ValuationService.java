package de.jhoffmann.api;


import de.jhoffmann.domain.Valuation;

import java.util.Date;

public interface ValuationService {
    Valuation value(String client, String tradeId, Date valuationDate);
}
