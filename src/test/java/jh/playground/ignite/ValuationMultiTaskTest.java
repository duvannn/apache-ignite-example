package jh.playground.ignite;

import jh.playground.ignite.api.TradeLookupService;
import jh.playground.ignite.domain.Trade;
import jh.playground.ignite.domain.Valuation;
import jh.playground.ignite.task.ValuationRequest;
import jh.playground.ignite.task.ValuationTaskImpl;
import jh.playground.ignite.task.ValuationTaskUsingServiceImpl;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.compute.ComputeTask;
import org.apache.ignite.lang.IgniteFuture;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class ValuationMultiTaskTest {
    public static void main(String[] args) {
        Ignition.setClientMode(true);
        // Why does it not work with client xml?
//        try (Ignite ignite = Ignition.start("examples/config/ironbridge/ib-client.xml")) {
        // So we need to start the slow val ctx too?
        try (Ignite ignite = Ignition.start("example-spring.xml")) {

            // Use trade lookup API
            TradeLookupService tradeSvc = ignite.services().serviceProxy("tradeService", TradeLookupService.class, false);
            IgniteCompute compute = ignite.compute().withAsync();

            Collection<IgniteFuture<Valuation>> valsF = new ArrayList<>(10);
            for (int i = 0; i < 10; i++) {
                Trade trade = tradeSvc.lookup(i);
                ValuationRequest req = new ValuationRequest(trade, new Date());

                System.out.println("Requesting valuation via task for trade " + trade);
                Valuation val = compute.execute(task(false), req);
//                IgniteFuture<Valuation> valF = new IgniteFinishedFutureImpl<>(val);
                IgniteFuture<Valuation> valF = compute.future();
                valsF.add(valF);
            }

            for (IgniteFuture<Valuation> valF : valsF) {
                System.out.println("Valued trade via task: " + valF.get());
            }
        }
    }

    private static Class<? extends ComputeTask<ValuationRequest, Valuation>> task(boolean usingService) {
        return usingService ? ValuationTaskUsingServiceImpl.class : ValuationTaskImpl.class;
    }
}
