package jh.playground.ignite.task;

import jh.playground.ignite.api.ValuationService;
import jh.playground.ignite.domain.Valuation;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.ComputeJobAdapter;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.ComputeTaskSplitAdapter;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.resources.ServiceResource;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ValuationTaskUsingServiceImpl extends ComputeTaskSplitAdapter<ValuationRequest, Valuation> {
    @IgniteInstanceResource
    private Ignite ignite;

    @ServiceResource(serviceName = "valuationService", proxySticky = false)
    private ValuationService valSvc;

    @Override
    protected Collection<? extends ComputeJob> split(int gridSize, ValuationRequest req) throws IgniteException {
        return Collections.singleton(new ComputeJobAdapter() {
            @Override
            public Object execute() throws IgniteException {
                String msg = String.format("ValuationsTask - [%s] - valuation request %s", Thread.currentThread().getName(), req);
                System.out.println(msg);

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
