package de.jhoffmann.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlowBean {
    private static final Logger log = LoggerFactory.getLogger(SlowBean.class);

    public SlowBean() throws InterruptedException {
        log.info("Initializing slow bean...");
        Thread.sleep(5000L);
        log.info("Done initializing slow bean");
    }
}
