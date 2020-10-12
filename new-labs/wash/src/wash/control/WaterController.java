package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;

public class WaterController extends ActorThread<WashingMessage> {
	private long dt = 3000; //ms
    private double mu = dt * 0.0478;
    private double ml = dt * 9.52 * Math.pow(10, -4);
    private WashingIO io;
    private WashingMessage currentMessage;
	private double targetTemp;

    public WaterController(WashingIO io) {
        this.io = io;
    }

    @Override
    public void run() {
    	try {
            while (true) {
                // wait for up to a (simulated) minute for a WashingMessage
                WashingMessage recievedMessage = receiveWithTimeout(dt / Settings.SPEEDUP);
                if(recievedMessage != null) {
                	currentMessage = recievedMessage;
                }

                // if m is null, it means a minute passed and no message was received
                if (currentMessage != null) {
                    switch (currentMessage.getCommand()) {
                        case WashingMessage.WATER_DRAIN:
                        	while(io.getWaterLevel() != 0) {
                        		io.drain(true);
                        	}
                        	io.drain(false);
                            break;

                        case WashingMessage.WATER_FILL:
                        	while(io.getWaterLevel() < currentMessage.getCommand()) {
                        		io.fill(true);
                        	}
                        	io.fill(false);
                            break;

                        case WashingMessage.WATER_IDLE:
                            break;
                    }
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
