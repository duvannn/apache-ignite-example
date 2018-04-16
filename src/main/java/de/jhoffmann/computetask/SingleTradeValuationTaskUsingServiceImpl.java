package de.jhoffmann.computetask;

import de.jhoffmann.api.ValuationService;
import de.jhoffmann.domain.Valuation;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.compute.*;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.resources.LoggerResource;
import org.apache.ignite.resources.ServiceResource;
import org.apache.ignite.resources.TaskSessionResource;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SingleTradeValuationTaskUsingServiceImpl extends ComputeTaskSplitAdapter<SingleTradeRequest, Valuation> {
    @IgniteInstanceResource
    private Ignite ignite;

    @LoggerResource
    private IgniteLogger log;

    @TaskSessionResource
    private ComputeTaskSession taskSes;

    @ServiceResource(serviceName = "valuationService") // proxySticky = false
    private ValuationService valSvc;

//    @SpringResource(resourceName = "slowBean")
//    private transient SlowBean slowBean;

    @Override
    protected Collection<? extends ComputeJob> split(int gridSize, SingleTradeRequest req) throws IgniteException {
        return Collections.singleton(new ComputeJobAdapter() {
            @Override
            public Object execute() throws IgniteException {
                String msg = String.format("%s (%s) valuing trade %s", taskSes.getTaskName(), taskSes.getId(), req);
                log.info(msg);

                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return valSvc.value("TestClient", req.trade.tradeId, req.valuationDate);
            }
        });
    }

    @Nullable
    @Override
    public Valuation reduce(List<ComputeJobResult> results) throws IgniteException {
        return results.get(0).getData();
    }
}
