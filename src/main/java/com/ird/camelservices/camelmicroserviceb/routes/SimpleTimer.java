package com.ird.camelservices.camelmicroserviceb.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SimpleTimer extends RouteBuilder {
    @Override
    public void configure() throws Exception {
            from("timer:simpleTimer?period=1000")
                    .routeId("simpleTimerId")
                    .log(LoggingLevel.INFO,"HelloWorld");
                    //.to("log:first-timer");

    }
}
