package jh.playground.ignite.service;

import jh.playground.ignite.api.ValuationService;
import jh.playground.ignite.domain.Trade;
import jh.playground.ignite.domain.Valuation;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.resources.LoggerResource;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

public class ValuationServiceImpl implements Service, ValuationService {
    @IgniteInstanceResource
    private Ignite ignite;

    @LoggerResource
    private IgniteLogger log;

//    @SpringResource(resourceName = "slowValuationsCtx")
//    private transient SlowValuationsCtx slowValuationsCtx;

    private IgniteCache<Integer, Trade> tradeCache;

    private int tradesValued;

    private Instant startedAt;
    private UUID serviceUuid;
    private String serviceName;

    @Override
    public void cancel(ServiceContext ctx) {
        String msg = String.format("%s (%s) is cancelled", ctx.name(), ctx.executionId());
        log.info(msg);
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
        log.info(msg);
    }

    @Override
    public Valuation value(String client, String tradeId, Date valuationDate) {
        String msg = String.format("%s (%s) valuing trade (%s, %s, %s)", serviceName, serviceUuid, client, tradeId, valuationDate);
        log.info(msg);
        tradesValued++;

        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new Valuation(tradesValued, valuationDate, 1.0, 2.0);
    }
}
