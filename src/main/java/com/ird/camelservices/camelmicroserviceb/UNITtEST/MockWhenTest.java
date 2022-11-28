package com.ird.camelservices.camelmicroserviceb.UNITtEST;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.apache.camel.test.junit5.TestSupport.header;
import static org.junit.jupiter.api.Assertions.*;

public class MockWhenTest extends CamelTestSupport {

    @Autowired OrderQueryProcessor oqp;

    //@Autowired

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

    private class OrderQueryProcessor implements Processor {
        public void process(Exchange exchange) throws Exception {
            String id = exchange.getIn().getBody(String.class);
            System.out.println("================id=====================" + id);
            exchange.getIn().setBody(id);
        }
    }
    private class OrderResponseProcessor implements Processor {
        public void process(Exchange exchange) throws Exception {
            String body = exchange.getIn().getBody(String.class);
            //String status = exchange.getIn().getHeader("STATUS", String.class);
            exchange.getIn().setBody(body);
        }
    }

    @Test
    public void testQuote() throws Exception {
        MockEndpoint quote = getMockEndpoint("mock:quote");
        quote.expectedMessageCount(1);
        template.sendBody("jms:topic:quote", "Camel rocks");
        quote.assertIsSatisfied();
    }



}
