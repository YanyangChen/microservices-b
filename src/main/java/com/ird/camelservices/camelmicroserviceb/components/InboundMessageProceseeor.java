package com.ird.camelservices.camelmicroserviceb.components;


import com.ird.camelservices.camelmicroserviceb.beans.NameAddress;
import com.ird.camelservices.camelmicroserviceb.beans.OutboundNameAddress;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InboundMessageProceseeor implements Processor {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void process(Exchange exchange) throws Exception {
        NameAddress filedata = exchange.getIn().getBody(NameAddress.class);
        exchange.getIn().setBody(new OutboundNameAddress(filedata.getName(), returnOutboundAddress(filedata)));
       // logger.info("This is the read fileData: " +filedata.toString());
    }

    private String returnOutboundAddress(NameAddress nameAddress){
        StringBuilder concatenatedAddress = new StringBuilder(200);
//        concatenatedAddress.append(nameAddress.getHouseNumber());
//        concatenatedAddress.append(nameAddress.getCity() + ",");
        concatenatedAddress.append(nameAddress.getProvince());
        concatenatedAddress.append(nameAddress.getPostalCode());
        return concatenatedAddress.toString();
    }
}
