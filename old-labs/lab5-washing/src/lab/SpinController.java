package lab;

import wash.WashingIO;

public class SpinController extends MessagingThread<WashingMessage> {

    // TODO: add attributes

    public SpinController(WashingIO io) {
        // TODO
    }

    @Override
    public void run() {
        try {

            // ... TODO ...

            while (true) {
                // wait for up to a (simulated) minute for a WashingMessage
                WashingMessage m = receiveWithTimeout(60000 / Wash.SPEEDUP);

                // if m is null, it means a minute passed and no message was received
                if (m != null) {
                    System.out.println("got " + m);
                }
                
                // ... TODO ...
            }
        } catch (InterruptedException unexpected) {
            // we don't expect this thread to be interrupted,
            // so throw an error if it happens anyway
            throw new Error(unexpected);
        }
    }
}
