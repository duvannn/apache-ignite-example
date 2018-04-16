package de.jhoffmann.service;

import de.jhoffmann.api.ValuationService;
import de.jhoffmann.domain.Trade;
import de.jhoffmann.domain.Valuation;
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
import java.util.concurrent.atomic.AtomicInteger;

public class ValuationServiceImpl implements Service, ValuationService {
    @IgniteInstanceResource
    private Ignite ignite;

    @LoggerResource
    private IgniteLogger log;

//    @SpringResource(resourceName = "slowBean")
//    private transient SlowBean slowBean;

    private IgniteCache<Integer, Trade> tradeCache;

    private AtomicInteger tradesValued = new AtomicInteger(0);

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
        int pkey = tradesValued.incrementAndGet();
        log.info(String.format("%s (%s) valuing trade (%s, %s, %s) - %s", serviceName, serviceUuid, client, tradeId, valuationDate, "" + tradesValued.hashCode() + " -" + pkey));

        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info(String.format("%s (%s) valued trade (%s, %s, %s) - %s", serviceName, serviceUuid, client, tradeId, valuationDate, "" + tradesValued.hashCode() + " -" + pkey));
        return new Valuation(pkey, valuationDate, 1.0, 2.0, tradeId);
    }
}
