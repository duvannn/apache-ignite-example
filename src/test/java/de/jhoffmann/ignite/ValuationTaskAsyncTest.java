package de.jhoffmann;

import de.jhoffmann.api.TradeLookupService;
import de.jhoffmann.computetask.SingleTradeRequest;
import de.jhoffmann.computetask.SingleTradeValuationTaskImpl;
import de.jhoffmann.computetask.SingleTradeValuationTaskUsingServiceImpl;
import de.jhoffmann.domain.Trade;
import de.jhoffmann.domain.Valuation;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.compute.ComputeTask;
import org.apache.ignite.lang.IgniteFuture;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class ValuationTaskAsyncTest {
    public static void main(String[] args) {
        Ignition.setClientMode(true);
        try (Ignite ignite = Ignition.start("example-ignite.xml")) {
            // Use trade lookup API
            TradeLookupService tradeSvc = ignite.services().serviceProxy("tradeService", TradeLookupService.class, false);
            IgniteCompute compute = ignite.compute().withAsync();

            Collection<IgniteFuture<Valuation>> valsF = new ArrayList<>(10);
            for (int i = 0; i < 10; i++) {
                Trade trade = tradeSvc.lookup(i);
                SingleTradeRequest req = new SingleTradeRequest(trade, new Date());

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

    private static Class<? extends ComputeTask<SingleTradeRequest, Valuation>> task(boolean usingService) {
        return usingService ? SingleTradeValuationTaskUsingServiceImpl.class : SingleTradeValuationTaskImpl.class;
    }
}
