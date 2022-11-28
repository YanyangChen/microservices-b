package com.ird.camelservices.camelmicroserviceb.UNITtEST;

import com.ird.camelservices.camelmicroserviceb.processors.SimpleLoggingProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class RouteTest extends CamelTestSupport {
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("jms:topic:quote")
                        .choice()
                        .when(header("Counter1").isEqualTo(1))
                        .to("mock:quo1")
                        .when(header("Counter2").isEqualTo(2))
                        .to("mock:quo2")
                        .otherwise()
                        .to("mock:quo3");
            }
        };
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();
        context.addComponent("jms", context.getComponent("seda"));
        return context;
    }

    @Test
    public void testMockWithHeaders() throws Exception {
        //getMockEndpoint("mock:quo").expectedBodiesReceived("Hello World with Header");
        getMockEndpoint("mock:quo1").expectedHeaderReceived("Counter1",1);
        getMockEndpoint("mock:quo2").expectedHeaderReceived("Counter2",2);

        HashMap hm1 = new HashMap<>();
        hm1.put("Counter1",1);
        template.sendBodyAndHeaders("jms:topic:quote", "Hello World with Header1", hm1);


        HashMap hm2 = new HashMap<>();
        hm2.put("Counter2",2);
        template.sendBodyAndHeaders("jms:topic:quote", "Hello World with Header2", hm2);

        assertMockEndpointsSatisfied();
    }




}
