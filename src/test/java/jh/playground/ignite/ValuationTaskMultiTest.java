package jh.playground.ignite;

import jh.playground.ignite.api.TradeLookupService;
import jh.playground.ignite.computetask.MultiTradesRequest;
import jh.playground.ignite.computetask.MultiTradesValuationTaskImpl;
import jh.playground.ignite.computetask.MultiTradesValuationTaskUsingServiceImpl;
import jh.playground.ignite.domain.Trade;
import jh.playground.ignite.domain.Valuation;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.compute.ComputeTask;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ValuationTaskMultiTest {
    public static void main(String[] args) {
        Ignition.setClientMode(true);
        try (Ignite ignite = Ignition.start("example-ignite.xml")) {
            // Use trade lookup API
            TradeLookupService tradeSvc = ignite.services().serviceProxy("tradeService", TradeLookupService.class, false);
            IgniteCompute compute = ignite.compute();

            List<Trade> trades = IntStream.range(0, 10).mapToObj(tradeSvc::lookup).collect(Collectors.toList());
            MultiTradesRequest req = new MultiTradesRequest(trades, new Date());

            System.out.println("Requesting valuations via task for request " + req);
            Collection<Valuation> vals = compute.execute(task(false), req);

            for (Valuation val : vals) {
                System.out.println("Valued trade via task: " + val);
            }
        }
    }

    private static Class<? extends ComputeTask<MultiTradesRequest, Collection<Valuation>>> task(boolean usingService) {
        return usingService ? MultiTradesValuationTaskUsingServiceImpl.class : MultiTradesValuationTaskImpl.class;
    }

}
