package com.ird.camelservices.camelmicroserviceb.routes;

import com.ird.camelservices.camelmicroserviceb.components.SimpleLoggingProcessingComponent;
import com.ird.camelservices.camelmicroserviceb.processors.SimpleLoggingProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;


//@Component
public class LoggingRouter extends RouteBuilder {

    //@Autowired
    //private GetCurrentTimeBean getCurrentTimeBean;

    @Autowired
    private SimpleLoggingProcessingComponent loggingComponent;

    @Override
    public void configure() throws Exception {
        //timer
        //transformation
        //log
        //Exchange[ExchangePattern: InOnly, BodyType: null, Body: [Body is null]]
        from("jms:topic:quote")//queue
                //.log("${body}")
                //.transform().constant("My Constant Message")

                //.transform().constant("Time now is" + LocalDateTime.now())
                //.bean("getCurrentTimeBean")

                //Processing : making NO change of the message body itself
                //Transformation making changes of the message body itself

                //.bean(getCurrentTimeBean,"getCurrentTime") //no need to specify method name when there's only one method in the bean
                .log("${body}")
                .bean(loggingComponent)
                .log("${body}")
                .bean(new SimpleLoggingProcessor())
                .log("${body}")
                .to("mock:quote");//database


    }
}

