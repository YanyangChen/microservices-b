package com.ird.camelservices.camelmicroserviceb.processors;

import com.ird.camelservices.camelmicroserviceb.components.SimpleLoggingProcessingComponent;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleLoggingProcessor implements Processor {

    private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessingComponent.class);

    @Override
    public void process(Exchange exchange) throws Exception {

        logger.info("SimpleLoggingProcessor testing {}", exchange.getMessage().getBody());
        logger.info("SimpleLoggingProcessor testing Header {}", exchange.getMessage().getHeader("Counter"));
        logger.info("SimpleLoggingProcessor testing Header {}", exchange.getMessage().getHeader("Counter2"));
        logger.error("SimpleLoggingProcessor testing {}", exchange.getMessage().getBody());
    }
}