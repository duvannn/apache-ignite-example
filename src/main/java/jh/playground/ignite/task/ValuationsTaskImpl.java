package jh.playground.ignite.task;

import jh.playground.ignite.domain.Valuation;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.compute.*;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.resources.TaskSessionResource;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ValuationsTaskImpl extends ComputeTaskSplitAdapter<ValuationsRequest, Collection<Valuation>> {
    @IgniteInstanceResource
    private Ignite ignite;

    @TaskSessionResource
    private ComputeTaskSession taskSes;

//    @SpringResource(resourceName = "slowValuationsCtx")
//    private transient SlowValuationsCtx slowValuationsCtx;

    @Override
    protected Collection<? extends ComputeJob> split(int gridSize, ValuationsRequest req) throws IgniteException {
        PrimitiveIterator.OfInt tradesValued = IntStream.iterate(0, i -> i + 1).iterator();
        return req.trades.stream().map(t -> new ComputeJobAdapter() {
            @Override
            public Object execute() throws IgniteException {
                String msg = String.format("%s (%s) - [%s] - valuation requested for trade %s",
                        taskSes.getTaskName(), taskSes.getId(), Thread.currentThread().getName(), t);
                System.out.println(msg);

                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return new Valuation(tradesValued.nextInt(), req.valuationDate, 1.5, 2.5);
            }
        }).collect(Collectors.toList());
    }

    @Nullable
    @Override
    public Collection<Valuation> reduce(List<ComputeJobResult> results) throws IgniteException {
        return results.stream().map(ComputeJobResult::<Valuation>getData).collect(Collectors.toList());
    }
}
