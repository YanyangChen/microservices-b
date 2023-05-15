package com.ird.camelservices.camelmicroserviceb.UNITtEST;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.dataformat.SoapJaxbDataFormat;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class SOAPTest extends CamelTestSupport {
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
            return new SoapRoute();
    }

    public class SoapRoute extends RouteBuilder {
        String WS_URI = "https://203.142.127.224:18318/SendSmsService/services/SendSms/v3";
        //SoapJaxbDataFormat soapDF = new SoapJaxbDataFormat("com.example.customerservice", new ServiceInterfaceStrategy(CustomerService.class));
        @Override
        public void configure() throws Exception {
            from("direct:sendSoapRequest")
                    .setHeader("SOAPAction", constant("Some SOAP Action"))
                    .to(WS_URI)
                    .process(exchange -> {
                        String response = exchange.getIn().getBody(String.class);
                        System.out.println("SOAP Response: " + response);
                    });;
//            from(WS_URI)
//                    .to("mock:result");
        }
    }



    @Test
    public void testSendSoapReq() throws Exception {

        String soapRequest =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\""
                + " xmlns:loc=\"http://www.csapi.org/schema/parlayx/sms/send/3_1/local\">"
                + "<soapenv:Header>"
                + "<RequestSOAPHeader xmlns=\"http://www.huawei.com.cn/schema/common/v2_1\">"
                + "<spId>A2P_IRD</spId>"
                + "<spPassword>ff769c893c035b2625996f72cf828cbb</spPassword>"
                + "<serviceId>A2P_IRD</serviceId>"
                + "<timeStamp>20230510</timeStamp>"
                + "<OA>8525122889143</OA>"
                + "<FA>8525122889143</FA>"
                + "<linkid>8525122889143</linkid>"
                + "</RequestSOAPHeader>"
                + "</soapenv:Header>"
                + "<soapenv:Body>"
                + "<loc:sendSms>"
                + "<loc:addresses>[Mobile Number]</loc:addresses>"
                + "<loc:senderName>8525122889143</loc:senderName>"
                + "<loc:message>Hello World</loc:message>"
                + "</loc:sendSms>"
                + "</soapenv:Body>"
                + "</soapenv:Envelope>";

        template.sendBody("direct:sendSoapRequest", soapRequest);


        String expectedResponse = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                + "<soapenv:Body>"
                + "<loc:sendSmsResponse xmlns:loc=\"http://www.csapi.org/schema/parlayx/sms/send/3_1/local\">"
                + "<loc:result>OK</loc:result>"
                + "<loc:resultCode>0</loc:resultCode>"
                + "<loc:resultDescription>Success</loc:resultDescription>"
                + "<loc:extendParam>"
                + "<loc:name></loc:name>"
                + "<loc:value></loc:value>"
                + "</loc:extendParam>"
                + "</loc:sendSmsResponse>"
                + "</soapenv:Body>"
                + "</soapenv:Envelope>";

//        //assertEquals("Hello World", content);
//        MockEndpoint mockEndpoint = getMockEndpoint("mock:result");
//        mockEndpoint.expectedMessageCount(1);
//
//        // Assert that the SOAP response matches the expected response
//        Exchange receivedExchange = mockEndpoint.assertExchangeReceived(0);
//        String receivedResponse = receivedExchange.getIn().getBody(String.class);
//        assertEquals(expectedResponse, receivedResponse);


    }
}