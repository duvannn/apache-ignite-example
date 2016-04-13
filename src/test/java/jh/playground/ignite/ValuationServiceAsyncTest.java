package jh.playground.ignite;

import jh.playground.ignite.api.ValuationService;
import jh.playground.ignite.domain.Valuation;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteServices;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteFuture;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class ValuationServiceAsyncTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Ignition.setClientMode(true);
        try (Ignite ignite = Ignition.start("example-ignite.xml")) {
            // Use valuations API
            IgniteServices svcs = ignite.services().withAsync();
            ValuationService valSvc = svcs.serviceProxy("valuationService", ValuationService.class, false);

            Collection<IgniteFuture<Valuation>> valsF = new ArrayList<>(10);
            for (int i = 0; i < 10; i++) {
                String tradeId = "TID_" + i;
                System.out.println("Requesting valuation via service for trade " + tradeId);
                Valuation val = valSvc.value("TestClient", tradeId, new Date());
//                IgniteFuture<Valuation> valF = new IgniteFinishedFutureImpl<>(val);
                IgniteFuture<Valuation> valF = svcs.future();
                valsF.add(valF);
            }

            for (IgniteFuture<Valuation> valF : valsF) {
                System.out.println("Valued trade via service: " + valF.get());
            }
        }
    }
}
