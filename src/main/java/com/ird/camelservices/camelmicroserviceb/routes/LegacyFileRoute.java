package com.ird.camelservices.camelmicroserviceb.routes;

import com.ird.camelservices.camelmicroserviceb.beans.NameAddress;
import com.ird.camelservices.camelmicroserviceb.components.InboundMessageProceseeor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.beanio.BeanIODataFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LegacyFileRoute extends RouteBuilder {

    Logger logger = LoggerFactory.getLogger(getClass());
    BeanIODataFormat inboundDataformat = new BeanIODataFormat("inboundMessageBeanIOMapping.xml","inputMessageStream");
    @Override
    public void configure() throws Exception {

        from("file:src/data/input?fileName=inputFile.csv&noop=true")
                .messageHistory()
                .routeId("legacyFileMoveRouteId")
                .split(body().tokenize("\n",1,true))
                .unmarshal(inboundDataformat)
                .process(
                    new InboundMessageProceseeor()
                    //exchange.getIn().setBody(filedata.toString());
                )
                .log(LoggingLevel.INFO, "Transformed body + ${body}")
                .convertBodyTo(String.class)
                .to("file:src/data/output?fileName=outputFile.txt&fileExist=append&appendChars=\\n")
                .end();
    }
}
