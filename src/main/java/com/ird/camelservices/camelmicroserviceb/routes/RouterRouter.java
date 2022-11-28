package com.ird.camelservices.camelmicroserviceb.routes;

import org.apache.camel.builder.RouteBuilder;

public class RouterRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:a")
                .choice()
                .when(simple("${header.foo} == 'bar'"))
                .to("direct:b")
                .when(simple("${header.foo} == 'cheese'"))
                .to("direct:c")
                .otherwise()
                .to("direct:d");
    }
}
