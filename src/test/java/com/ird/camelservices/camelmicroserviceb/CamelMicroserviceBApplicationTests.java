package com.ird.camelservices.camelmicroserviceb;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@CamelSpringBootTest
@UseAdviceWith //allow router id reference
class CamelMicroserviceBApplicationTests extends CamelTestSupport {
	@Autowired
	CamelContext context;



	@EndpointInject("mock:result")
	protected MockEndpoint mockEndpoint;

	@Test
	public void testSimpleTimer() throws Exception{
		 String expectedBody = "HelloWorld from body";

		 mockEndpoint.expectedBodiesReceived(expectedBody);
		 mockEndpoint.expectedMinimumMessageCount(1);

		AdviceWith.adviceWith(context, "simpleTimerId", routeBuilder ->{
			routeBuilder.weaveAddLast().to(mockEndpoint);
		});

		context.start();
		mockEndpoint.assertIsSatisfied();
	}

	@Test
	void contextLoads() {
	}

}
