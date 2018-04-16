package de.jhoffmann;

import de.jhoffmann.api.ValuationService;
import de.jhoffmann.domain.Valuation;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteServices;
import org.apache.ignite.Ignition;

import java.util.Date;
import java.util.concurrent.ExecutionException;

public class ValuationServiceSequentialTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Ignition.setClientMode(true);
        try (Ignite ignite = Ignition.start("example-ignite.xml")) {
            // Use valuations API
            IgniteServices svcs = ignite.services();
            ValuationService valSvc = svcs.serviceProxy("valuationService", ValuationService.class, false);

            for (int i = 0; i < 5; i++) {
                String tradeId = "TID_" + i;
                System.out.println("Requesting valuation via service for trade " + tradeId);

                Valuation val = valSvc.value("TestClient", tradeId, new Date());
                System.out.println("Valued trade via service: " + val);
            }
        }
    }

}
