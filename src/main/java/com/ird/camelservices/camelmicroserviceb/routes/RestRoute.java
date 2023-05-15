package com.ird.camelservices.camelmicroserviceb.routes;

import com.ird.camelservices.camelmicroserviceb.beans.NameAddress;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class RestRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        restConfiguration()
                .component("jetty")
                .host("0.0.0.0")
                .port(8080)
                .bindingMode(RestBindingMode.json)
                .enableCORS(true);

        rest("masterclass")
                .produces("application/json")
                .post("nameAddress").type(NameAddress.class).route()
                .routeId("RestRouteId")
                .log(LoggingLevel.INFO, "Transformed body : ${body}")
                //.marshal().protobuf()
                .log(LoggingLevel.INFO, "Transformed protobuf body : ${body}")
                .convertBodyTo(String.class)
                .to("file:src/data/output?fileName=outputFile.txt&fileExist=append&appendChars=\\n");
//                .to("activemq:queue:nameaddressqueue?exchangePattern=InOnly")
//                .to("jpa:"+NameAddress.class.getName());
    }
}
