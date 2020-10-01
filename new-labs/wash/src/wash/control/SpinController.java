package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;

public class SpinController extends ActorThread<WashingMessage> {

    // TODO: add attributes
    private WashingIO io;
    private int currentSpin;
    private WashingMessage currentMessage;

    public SpinController(WashingIO io) {
        this.io = io;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // wait for up to a (simulated) minute for a WashingMessage
                WashingMessage recievedMessage = receiveWithTimeout(60000 / Settings.SPEEDUP);
                if(recievedMessage != null) {
                	currentMessage = recievedMessage;
                }

                // if m is null, it means a minute passed and no message was received
                if (currentMessage != null) {
                    switch (currentMessage.getCommand()) {
                        case WashingMessage.SPIN_SLOW:
                            if(currentSpin == WashingIO.SPIN_LEFT) {
                                currentSpin = WashingIO.SPIN_RIGHT;
                            } else {
                                currentSpin = WashingIO.SPIN_LEFT;
                            }
                            break;

                        case WashingMessage.SPIN_FAST:
                            currentSpin = WashingIO.SPIN_FAST;
                            break;

                        case WashingMessage.SPIN_OFF:
                            currentSpin = WashingIO.SPIN_IDLE;
                            break;
                    }
                    io.setSpinMode(currentSpin);
                    currentMessage.getSender().send(new WashingMessage(this, WashingMessage.ACKNOWLEDGMENT));
                }
            }
        } catch (InterruptedException unexpected) {
            // we don't expect this thread to be interrupted,
            // so throw an error if it happens anyway
            throw new Error(unexpected);
        }
    }
}
