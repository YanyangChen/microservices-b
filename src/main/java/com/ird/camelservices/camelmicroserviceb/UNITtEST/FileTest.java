package com.ird.camelservices.camelmicroserviceb.UNITtEST;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.apache.camel.test.junit5.TestSupport.deleteDirectory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class FileTest extends CamelTestSupport {
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
            return new FileMoveRoute();
    }

    public class FileMoveRoute extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("file://target/inbox").to("file://target/outbox");
        }
    }

//    public void setUp() throws Exception {
//        deleteDirectory("target/inbox");
//        deleteDirectory("target/outbox");
//        super.setUp();
//    }

    @Test
    public void testMoveFile() throws Exception {
        template.sendBodyAndHeader("file://target/inbox", "Hello World",
                Exchange.FILE_NAME, "hello.txt");
        Thread.sleep(1000);
        File target = new File("target/outbox/hello.txt");
        assertTrue("File not moved", target.exists());
        String content = context.getTypeConverter()
                .convertTo(String.class, target);
        assertEquals("Hello World", content);

    }
}