package jh.playground.ignite.service;

import jh.playground.ignite.api.TradeLookupService;
import jh.playground.ignite.domain.Trade;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

public class TradeLookupServiceImpl implements Service, TradeLookupService {
    @IgniteInstanceResource
    private Ignite ignite;

//    @SpringResource(resourceName = "slowValuationsCtx")
//    private transient SlowValuationsCtx slowValuationsCtx;

    private IgniteCache<Integer, Trade> tradeCache;

    private Instant startedAt;
    private UUID serviceUuid;
    private String serviceName;

    @Override
    public void cancel(ServiceContext ctx) {
        String msg = String.format("%s (%s) is cancelled", ctx.name(), ctx.executionId());
        System.out.println(msg);
    }

    @Override
    public void init(ServiceContext ctx) throws Exception {
        tradeCache = ignite.getOrCreateCache("Trades"); // get handle on the cache
        serviceUuid = ctx.executionId();
        serviceName = ctx.name();
    }

    @Override
    public void execute(ServiceContext ctx) throws Exception {
        startedAt = Instant.now();
        String msg = String.format("%s (%s) started at %s", ctx.name(), ctx.executionId(), ZonedDateTime.ofInstant(startedAt, ZoneId.of("UTC")));
        System.out.println(msg);
    }

    @Override
    public Trade lookup(int tradePkey) {
        String msg = String.format("%s (%s) [%s] looking up trade %d", serviceName, serviceUuid, Thread.currentThread().getName(), tradePkey);
        System.out.println(msg);

        return tradeCache.get(tradePkey);
    }
}
