package jh.playground.ignite.task;

import jh.playground.ignite.domain.Valuation;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.compute.*;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.resources.TaskSessionResource;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.stream.IntStream;

public class ValuationTaskImpl extends ComputeTaskSplitAdapter<ValuationRequest, Valuation> {
    @IgniteInstanceResource
    private Ignite ignite;

    @TaskSessionResource
    private ComputeTaskSession taskSes;

//    @SpringResource(resourceName = "slowValuationsCtx")
//    private transient SlowValuationsCtx slowValuationsCtx;

    @Override
    protected Collection<? extends ComputeJob> split(int gridSize, ValuationRequest req) throws IgniteException {
        PrimitiveIterator.OfInt tradesValued = IntStream.iterate(0, i -> i + 1).iterator();
        return Collections.singleton(new ComputeJobAdapter() {
            @Override
            public Object execute() throws IgniteException {
                String msg = String.format("%s (%s) - [%s] - valuation request %s",
                        taskSes.getTaskName(), taskSes.getId(), Thread.currentThread().getName(), req);
                System.out.println(msg);

                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return new Valuation(tradesValued.nextInt(), req.valuationDate, 1.5, 2.5);
            }
        });
    }

    @Nullable
    @Override
    public Valuation reduce(List<ComputeJobResult> results) throws IgniteException {
        return results.get(0).getData();
    }
}
