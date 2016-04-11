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
import java.util.List;
import java.util.stream.Collectors;

public class ValuationsTaskUsingServiceImpl extends ComputeTaskSplitAdapter<ValuationsRequest, Collection<Valuation>> {
    @IgniteInstanceResource
    private Ignite ignite;

    @ServiceResource(serviceName = "valuationService", proxySticky = false)
    private ValuationService valSvc;

    @Override
    protected Collection<? extends ComputeJob> split(int gridSize, ValuationsRequest req) throws IgniteException {
        return req.trades.stream().map(t -> new ComputeJobAdapter() {
            @Override
            public Object execute() throws IgniteException {
                String msg = String.format("ValuationsTask - [%s] - valuation requested for trade %s", Thread.currentThread().getName(), t);
                System.out.println(msg);

                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return valSvc.value("TestClient", t.tradeId, req.valuationDate);
            }
        }).collect(Collectors.toList());
    }

    @Nullable
    @Override
    public Collection<Valuation> reduce(List<ComputeJobResult> results) throws IgniteException {
        return results.stream().map(ComputeJobResult::<Valuation>getData).collect(Collectors.toList());
    }
}
