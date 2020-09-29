package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;

public class SpinController extends ActorThread<WashingMessage> {

    // TODO: add attributes
    private WashingIO io;
    private int previousDirection;

    public SpinController(WashingIO io) {
        this.io = io;
        // TODO
    }

    @Override
    public void run() {
        try {

            // ... TODO ...

            while (true) {
                // wait for up to a (simulated) minute for a WashingMessage
                WashingMessage m = receiveWithTimeout(60000 / Settings.SPEEDUP);

                // if m is null, it means a minute passed and no message was received
                if (m != null) {
                    System.out.println("got " + m);
                    switch (m.getCommand()) {
                        case WashingMessage.SPIN_SLOW:
                            if(previousDirection == io.SPIN_LEFT) {
                                io.setSpinMode(io.SPIN_RIGHT);
                            } else {
                                io.setSpinMode(io.SPIN_LEFT);
                            }
                            break;

                        case WashingMessage.SPIN_FAST:
                            io.setSpinMode(io.SPIN_FAST);
                            break;

                        case WashingMessage.SPIN_OFF:
                            io.setSpinMode(io.SPIN_IDLE);
                            break;
                    }
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
