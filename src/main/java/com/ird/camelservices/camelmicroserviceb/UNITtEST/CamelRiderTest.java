package com.ird.camelservices.camelmicroserviceb.UNITtEST;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.junit5.CamelSpringTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

import static org.apache.camel.test.junit5.TestSupport.deleteDirectory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class CamelRiderTest extends CamelSpringTestSupport {

    private String inboxDir = "file://rider/files/inbox";
    private String outboxDir = "file://rider/files/outbox";

//
//    public CamelRiderTest(String inboxDir) {
//        this.inboxDir = inboxDir;
//    }

//    public void setUp() throws Exception {
//        super.setUp();
//        inboxDir = context.resolvePropertyPlaceholders(
//                "{{file.inbox}}");
//        outboxDir = context.resolvePropertyPlaceholders(
//                "{{file.outbox}}");
//        deleteDirectory(inboxDir);
//        deleteDirectory(outboxDir);
//    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext(new String[]
                {"camelinaction/rider-camel-prod.xml",
                        "camelinaction/rider-came-test.xml"});
    }

    @Test
    public void testMoveFile() throws Exception {
        template.sendBodyAndHeader("file://target/inbox", "Hello World",
                Exchange.FILE_NAME, "hello.txt");
        Thread.sleep(1000);
        File target = new File(outboxDir + "/hello.txt");
        assertTrue("File not moved", target.exists());
        String content = context.getTypeConverter()
                .convertTo(String.class, target);
        assertEquals("Hello World", content);
    }
}
