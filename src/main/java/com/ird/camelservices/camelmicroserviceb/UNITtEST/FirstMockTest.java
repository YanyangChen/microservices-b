package com.ird.camelservices.camelmicroserviceb.UNITtEST;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

public class FirstMockTest extends CamelTestSupport {
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("jms:topic:quote").to("mock:quote");
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
    public void testQuote() throws Exception {
        MockEndpoint quote = getMockEndpoint("mock:quote");
        quote.expectedMessageCount(1);
        template.sendBody("jms:topic:quote", "Camel rocks");
        quote.assertIsSatisfied();
    }

    @Test
    public void testQuotes() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:quote");
        mock.expectedBodiesReceived("Camel rocks", "Hello Camel");
        template.sendBody("jms:topic:quote", "Camel rocks");
        template.sendBody("jms:topic:quote", "Hello Camel");
        mock.assertIsSatisfied();
    }
}
