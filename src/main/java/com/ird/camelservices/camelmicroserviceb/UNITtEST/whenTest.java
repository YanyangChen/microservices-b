package com.ird.camelservices.camelmicroserviceb.UNITtEST;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.apache.camel.test.junit5.params.Test;
import org.apache.camel.util.ObjectHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class whenTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("jms:topic:quote")
                        .process(new OrderQueryProcessor())
                        .to("mock:quote")
                        .process(new OrderResponseProcessor());
            }
        };
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();
        context.addComponent("jms", context.getComponent("seda"));
        return context;
    }
    private class OrderQueryProcessor
            implements Processor {
        public void process(Exchange exchange) throws Exception {
            String id = exchange.getIn().getHeader("id", String.class);
            exchange.getIn().setBody("ID=" + id);
        }
    }
    private class OrderResponseProcessor
            implements Processor {
        public void process(Exchange exchange) throws Exception {
            String body = exchange.getIn().getBody(String.class);
            //String reply = ObjectHelper.after(body, "STATUS=");
            exchange.getIn().setBody(body + ", STATUS=");
        }
    }

    @Test
    public void testMiranda() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:quote");
        mock.expectedBodiesReceived("ID=123");
        mock.whenAnyExchangeReceived(new Processor() {
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody("ID=123,STATUS=IN PROGRESS");
            }
        });
        String out = template.requestBody("jms:topic:quote", null, String.class);
        template.sendBody("jms:topic:quote", "ID=123");
        assertEquals("IN PROGRESS", out);
        assertMockEndpointsSatisfied();
    }
}


