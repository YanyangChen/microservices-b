package com.ird.camelservices.camelmicroserviceb.UNITtEST;

import com.ird.camelservices.camelmicroserviceb.processors.SimpleLoggingProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class MsgBusTest extends CamelTestSupport {
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("jms:topic:quote")
                        .log("****************************++++++++++++${body}")
                        .bean(new SimpleLoggingProcessor())
                        .log("****************************++++++++++++${body}")
                        .to("mock:quo");
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
    public void testMock() throws Exception {
        getMockEndpoint("mock:quo").expectedBodiesReceived("Hello World");

        template.sendBody("jms:topic:quote", "Hello World");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testMockWithHeader() throws Exception {
        getMockEndpoint("mock:quo").expectedBodiesReceived("Hello World with Header");

        //template.sendBodyAndHeader("jms:topic:quote", "Hello World with Header");
        template.sendBodyAndHeader("jms:topic:quote", "Hello World with Header", "Counter", 1);

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testMockWithHeaders() throws Exception {
        //getMockEndpoint("mock:quo").expectedBodiesReceived("Hello World with Header");
        getMockEndpoint("mock:quo").expectedHeaderReceived("Counter",1);
        getMockEndpoint("mock:quo").expectedHeaderReceived("Counter2",2);
        HashMap hm = new HashMap<>();
        hm.put("Counter",1);
        hm.put("Counter2",2);
        template.sendBodyAndHeaders("jms:topic:quote", "Hello World with Header", hm);

        assertMockEndpointsSatisfied();
    }


}
