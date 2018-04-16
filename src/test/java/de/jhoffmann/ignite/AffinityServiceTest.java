package de.jhoffmann;

import de.jhoffmann.api.TradeLookupService;
import de.jhoffmann.api.ValuationService;
import de.jhoffmann.domain.Trade;
import de.jhoffmann.domain.Valuation;
import de.jhoffmann.service.ValuationServiceImpl;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.IgniteServices;
import org.apache.ignite.Ignition;
import org.apache.ignite.compute.ComputeTaskFuture;
import org.apache.ignite.lang.IgniteClosure;
import org.apache.ignite.lang.IgniteFuture;
import org.apache.ignite.services.ServiceConfiguration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class AffinityServiceTest {


    /**
     * run valuations asynchronously on the nodes that are storing the given trade
     * @param args
     */
    public static void main(String[] args) {

        Ignition.setClientMode(true);
        try (Ignite ignite = Ignition.start("example-ignite.xml")) {


            IgniteCompute asyncCompute = ignite.compute().withAsync();
            final List<Integer> tradeKeys = IntStream.range(0, 10).boxed().collect(toList());
            final List<ComputeTaskFuture<Object>> futs = tradeKeys.stream().map(tradeKey -> {
                asyncCompute.affinityCall("Trades", tradeKey, () -> {
                    TradeLookupService tradeSvc = ignite.services().serviceProxy("tradeService", TradeLookupService.class, true);
                    final String tradeId = tradeSvc.lookup(tradeKey).tradeId;
                    final ValuationService valuationService = ignite.services().serviceProxy("valuationService", ValuationService.class, true);
                    System.out.println("INVOKE FOR " + tradeKey + "/" + tradeId);
                    return valuationService.value("TestClient", tradeId, new Date());
                });
                return asyncCompute.future();
            }).collect(Collectors.toList());
            System.out.println("DONE CALL, Waiting for futures...");
// Wait for all futures to complete.
            final List<Object> valuations = futs.stream().map(IgniteFuture::get).collect(toList());
            System.out.println(valuations);
        }
    }

}
//_ is it
