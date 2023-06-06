package com.ird.camelservices.camelmicroserviceb.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SimpleSoapTimer extends RouteBuilder {
    @Override
    public void configure() throws Exception {
            from("timer:simpleTimer?period=1000")
                    .routeId("simpleTimerId")
                    .setBody(constant("HelloWorld from body"))
                    .log(LoggingLevel.INFO,"${body}");
                    //.to("log:first-timer");

    }
}
