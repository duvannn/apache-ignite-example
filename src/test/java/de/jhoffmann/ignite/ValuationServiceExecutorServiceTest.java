package de.jhoffmann;

import de.jhoffmann.api.ValuationService;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteServices;
import org.apache.ignite.Ignition;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ValuationServiceExecutorServiceTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Ignition.setClientMode(true);
        try (Ignite ignite = Ignition.start("example-ignite.xml")) {
            // Use valuations API
            IgniteServices svcs = ignite.services();

            ValuationService valSvc = svcs.serviceProxy("valuationService", ValuationService.class, false);

            Stream<ValRequest> reqs = IntStream.range(0, 100).mapToObj(i -> new ValRequest("TestClient", "TID_" + i, new Date()));
            CompletableFuture[] vals = reqs.map(r -> CompletableFuture.supplyAsync(() -> {
                System.out.println("Requesting valuation via service for trade " + r.tradeId);
                return valSvc.value(r.client, r.tradeId, r.valuationDate);
            }).thenAccept(v -> System.out.println("Valued trade via service: " + v))).toArray(CompletableFuture[]::new);

            CompletableFuture.allOf(vals).join();
        }

    }

    private static class ValRequest {
        final String client;
        final String tradeId;
        final Date valuationDate;


        private ValRequest(String client, String tradeId, Date valuationDate) {
            this.client = client;
            this.tradeId = tradeId;
            this.valuationDate = valuationDate;
        }
    }
}
