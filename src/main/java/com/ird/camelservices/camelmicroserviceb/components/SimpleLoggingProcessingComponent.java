package com.ird.camelservices.camelmicroserviceb.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public
class SimpleLoggingProcessingComponent{
    private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessingComponent.class);
    public void process(String message){ // when the return type is void: means we are doing PROCESSING
        logger.info("SimpleLoggingProcessingComponent {}", message);
    }
}
