package jh.playground.ignite.util;

import jh.playground.ignite.domain.Trade;
import jh.playground.ignite.service.TradeLookupServiceImpl;
import jh.playground.ignite.service.ValuationServiceImpl;
import org.apache.ignite.*;

public class PrepareCluster {
    public static void main(String[] args) throws IgniteException {
        Ignition.setClientMode(true);
        try (Ignite ignite = Ignition.start("example-ignite.xml")) {
            // Caches
//            IgniteCache<Integer, Trade> cache = ignite.getOrCreateCache("Trades");
            initializeTradeCache();
            // Trade service
//            IgniteServices tradeSvc = ignite.services(ignite.cluster().forServers());
//            tradeSvc.deployClusterSingleton("tradeService", new TradeLookupServiceImpl());
            // Valuations service
//            IgniteServices valuationSvc = ignite.services(ignite.cluster().forServers());
//            valuationSvc.deployNodeSingleton("valuationService", new ValuationServiceImpl());
        }
    }

    private static void initializeTradeCache() {
        IgniteCache<Integer, Trade> cache = Ignition.ignite().cache("Trades");
        for (int i = 0; i < 10; i++) {
            cache.put(i, new Trade(i, "T_" + i, "Book_" + i, "Acc_" + i, "EquitySwap", "USD"));
        }
    }
}
