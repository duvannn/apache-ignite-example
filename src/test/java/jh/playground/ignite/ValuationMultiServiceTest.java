package jh.playground.ignite;

import jh.playground.ignite.api.ValuationService;
import jh.playground.ignite.domain.Valuation;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteServices;
import org.apache.ignite.Ignition;

import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ValuationMultiServiceTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Does not work with ignite.executorService()...
        ExecutorService ex = Executors.newFixedThreadPool(10);

        Ignition.setClientMode(true);
        try (Ignite ignite = Ignition.start("example-spring.xml")) {

            // Use valuations API
            IgniteServices svcs = ignite.services();

            ValuationService valSvc = svcs.serviceProxy("valuationService", ValuationService.class, false);

            List<Callable<Valuation>> tasks = IntStream.range(0, 10).mapToObj(i -> (Callable<Valuation>) () -> {
                String tradeId = "TID_" + i;
                System.out.println("Requesting valuation via service for trade " + tradeId);
                return valSvc.value("TestClient", tradeId, new Date());
            }).collect(Collectors.toList());

            List<Future<Valuation>> valsF = ex.invokeAll(tasks);

            for (Future<Valuation> valF : valsF) {
                System.out.println("Valued trade via service: " + valF.get());
            }
        }

        ex.shutdownNow();
        ex.awaitTermination(10, TimeUnit.SECONDS);

    }
}
