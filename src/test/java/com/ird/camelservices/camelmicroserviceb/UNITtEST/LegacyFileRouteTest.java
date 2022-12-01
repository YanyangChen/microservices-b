package com.ird.camelservices.camelmicroserviceb.UNITtEST;

import net.bytebuddy.asm.Advice;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@CamelSpringBootTest
@UseAdviceWith
public class LegacyFileRouteTest {
    @Autowired
    CamelContext context;

    @EndpointInject("mock:result")
    protected MockEndpoint mockEndpoint;

    @Autowired
    ProducerTemplate producerTemplate;

    @Test
    public void testFIleMove() throws Exception{

        //setup the mock
        String expectedBody = "This is the input file";
        mockEndpoint.expectedBodiesReceived(expectedBody);
        mockEndpoint.expectedMinimumMessageCount(1);


        //Tweak the route definition
        AdviceWith.adviceWith(context, "legacyFileMoveRouteId", routerBuilder ->{
            routerBuilder.weaveByToUri("file:*").replace().to(mockEndpoint);
        });

        //Start the context and validate if mock is asserted
        context.start();
        mockEndpoint.assertIsSatisfied();
    }

    @Test
    public void testFIleMoveByMockingFromEndpoint() throws Exception{
        //setup the mock
        String expectedBody = "OutboundNameAddress(name=Mike, address= ON MSF3D2)";
        mockEndpoint.expectedBodiesReceived(expectedBody);
        mockEndpoint.expectedMinimumMessageCount(1);
        //Tweak the route definition
        AdviceWith.adviceWith(context, "legacyFileMoveRouteId", routerBuilder ->{
            routerBuilder.replaceFromWith("direct:mockStart");
            routerBuilder.weaveByToUri("file:*").replace().to(mockEndpoint);
        });
        //Start the context and validate if mock is asserted
        context.start();
        producerTemplate.sendBody("direct:mockStart","name, house_number, city, province, postal_code\n" +
                "Mike, 111, Toronto, ON, MSF3D2");
        mockEndpoint.assertIsSatisfied();
    }
}
