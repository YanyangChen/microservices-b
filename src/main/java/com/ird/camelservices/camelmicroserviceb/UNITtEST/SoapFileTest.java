package com.ird.camelservices.camelmicroserviceb.UNITtEST;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.apache.camel.builder.Builder.constant;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class SoapFileTest extends CamelTestSupport {
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
            return new FileMoveRoute();
    }

    public class FileMoveRoute extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            //int retries = 3; // Number of retries
           // from("file://target/soapFiles?noop=true")
            from("direct:soapFiles")
                    .setHeader("Content-Type", constant("application/xml"))
                    .errorHandler(
                            defaultErrorHandler()
                                    .maximumRedeliveries(2)
                                    .redeliveryDelay(2000)
                                    .backOffMultiplier(2)
                                    .retryAttemptedLogLevel(LoggingLevel.WARN)
                                    .retryAttemptedLogInterval(5000)
                                    .onRedelivery(new Processor() {
                                        public void process(Exchange exchange) throws Exception {
                                            Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
                                            Integer counter = exchange.getProperty(Exchange.REDELIVERY_COUNTER, Integer.class);
                                            String message = String.format("Retry attempt %d due to: %s", counter, cause.getMessage());
                                            exchange.getIn().setHeader("Retry-Reason", message);
                                            exchange.getIn().setHeader("Retry-Counter", counter);
                                        }
                                    })
                    )
                    .to("http://localhost:8080/soap")
                    .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                    .end();
        }
    }

    class XmlFileReader {
        public static String readToString(String filePath) throws Exception {
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
            return new String(fileBytes, "UTF-8");
        }
    }

//    public void setUp() throws Exception {
//        deleteDirectory("target/inbox");
//        deleteDirectory("target/outbox");
//        super.setUp();
//    }

    @Test
    public void test503() throws Exception {
//        template.sendBodyAndHeader("file://target/soapTest", "Hello World",
//                Exchange.FILE_NAME, "hello.txt");
        String xmlString = XmlFileReader.readToString("/home/aaron/pccw/camel-microservice-b/soapFile/request503.xml");
        // Send the SOAP request file to the route
        template.sendBodyAndHeader("direct:soapFiles", xmlString, Exchange.FILE_NAME, "request_503.xml");

        // Wait for the route to complete
        Thread.sleep(1000);

        // Assert that the SOAP request was sent successfully
        //assertMockEndpointsSatisfied();
    }

    @Test
    public void test200() throws Exception {
//        template.sendBodyAndHeader("file://target/soapTest", "Hello World",
//                Exchange.FILE_NAME, "hello.txt");
        String xmlString200 = XmlFileReader.readToString("/home/aaron/pccw/camel-microservice-b/soapFile/requestHello.xml");
        // Send the SOAP request file to the route
        template.sendBodyAndHeader("direct:soapFiles", xmlString200, Exchange.FILE_NAME, "requestHello.xml");

        // Wait for the route to complete
        Thread.sleep(1000);

        // Assert that the SOAP request was sent successfully
        //assertMockEndpointsSatisfied();
    }



    }
