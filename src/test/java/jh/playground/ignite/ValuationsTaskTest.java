package jh.playground.ignite;

import jh.playground.ignite.api.TradeLookupService;
import jh.playground.ignite.domain.Trade;
import jh.playground.ignite.domain.Valuation;
import jh.playground.ignite.task.ValuationsRequest;
import jh.playground.ignite.task.ValuationsTaskImpl;
import jh.playground.ignite.task.ValuationsTaskUsingServiceImpl;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.compute.ComputeTask;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ValuationsTaskTest {
    public static void main(String[] args) {
        Ignition.setClientMode(true);
        // Why does it not work with client xml?
        try (Ignite ignite = Ignition.start("examples/config/ironbridge/ib-client.xml")) {
            // So we need to start the slow val ctx too?
//        try (Ignite ignite = Ignition.start("examples/config/ironbridge/ib.xml")) {

            // Use trade lookup API
            TradeLookupService tradeSvc = ignite.services().serviceProxy("tradeService", TradeLookupService.class, false);
            IgniteCompute compute = ignite.compute();

            List<Trade> trades = IntStream.range(0, 10).mapToObj(tradeSvc::lookup).collect(Collectors.toList());
            ValuationsRequest req = new ValuationsRequest(trades, new Date());

            System.out.println("Requesting valuations via task for request " + req);
            Collection<Valuation> vals = compute.execute(task(false), req);

            for (Valuation val : vals) {
                System.out.println("Valued trade via task: " + val);
            }
        }
    }

    private static Class<? extends ComputeTask<ValuationsRequest, Collection<Valuation>>> task(boolean usingService) {
        return usingService ? ValuationsTaskUsingServiceImpl.class : ValuationsTaskImpl.class;
    }

}
