package com.ird.camelservices.camelmicroserviceb.UNITtEST;

import com.ird.camelservices.camelmicroserviceb.beans.NameAddress;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

public class FormatTest extends CamelTestSupport {
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                //from("jms:topic:quote").to("mock:quote");

                restConfiguration()
                        .component("jetty")
                        .host("0.0.0.0")
                        .port(8080)
                        .bindingMode(RestBindingMode.json)
                        .enableCORS(true);


                from("direct:start")
                        .to("rest:post:nameAddress")
                        .to("mock:quote");
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

    //template.sendBodyAndHeader("direct:start", null, "me", "Donald Duck");

    @Test
    public void testRestBodyHeader() throws Exception {
        NameAddress nameAddress = new NameAddress();
        nameAddress.setName("test");
        nameAddress.setCity("c");
        nameAddress.setProvince("p");
        nameAddress.setHouseNumber("h");
        nameAddress.setPostalCode("pc");
        MockEndpoint quote = getMockEndpoint("mock:quote");
        quote.expectedMessageCount(1);
        template.sendBody("direct:start", nameAddress);
        quote.assertIsSatisfied();
    }
}
