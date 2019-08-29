package messagingtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

class MessagingTest {

    @Test
    void testBidirectional() {
        checkMainPrints(ExampleBidirectional::main,
                "request sent by ClientThread\n" + 
                "request received by FibonacciThread\n" + 
                "received result fib(14) = 377\n" + 
                "FibonacciThread terminated\n");
    }

    @Test
    void testProducerConsumer() {
        checkMainPrints(ExampleProducerConsumer::main,
                "consumer eagerly awaiting messages...\n" + 
                "received [ole]\n" + 
                "received [dole]\n" + 
                "received [doff]\n" + 
                "all done\n");
    }

    @Test
    void testMessagingWithTimeout() {
        checkMainPrints(ExampleMessagingWithTimeout::main,
                "consumer eagerly awaiting messages...\n" + 
                "received [ole]\n" + 
                "received [dole]\n" + 
                "received [null]\n" + 
                "received [doff]\n" + 
                "all done\n");
    }

    // -----------------------------------------------------------------------
    
    /** Helper interface for making lambdas, for a main function that throws InterruptedException */
    private interface InterruptibleMain {
        void invoke(String[] args) throws InterruptedException;
    }
    
    /** Helper method: run a main method in another class, and check the printed output. */ 
    private void checkMainPrints(InterruptibleMain main, String expectedOutput) {
        PrintStream sysout = System.out;

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(bos, true));

            main.invoke(new String[] {});
            assertEquals(expectedOutput, bos.toString());
        } catch (InterruptedException e) {
            fail("unexpected: " + e);
        } finally {
            System.setOut(sysout);
        }
    }

}
