package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;

public class WaterController extends ActorThread<WashingMessage> {
	private long dt = 3000; //ms
    private WashingIO io;
    private WashingMessage currentMessage;
    private boolean acked;

    public WaterController(WashingIO io) {
        this.io = io;
        this.acked = true;
    }

    @Override
    public void run() {
    	try {
            while (true) {
                // wait for up to a (simulated) minute for a WashingMessage
                WashingMessage recievedMessage = receiveWithTimeout(dt / Settings.SPEEDUP);
                if(recievedMessage != null) {
                	currentMessage = recievedMessage;
                	acked = false;
                }

                // if m is null, it means a minute passed and no message was received
                if (currentMessage != null) {
                    switch (currentMessage.getCommand()) {
                        case WashingMessage.WATER_DRAIN:
                        	io.fill(false);
                        	if(io.getWaterLevel() > 0) {
                        		io.drain(true);
                        	}
                            if(!acked && io.getWaterLevel() <= 0) {
                            	currentMessage.getSender().send(new WashingMessage(this, WashingMessage.ACKNOWLEDGMENT));
                            	acked = true;
                            }
                            break;

                        case WashingMessage.WATER_FILL:
                        	io.drain(false);
                        	if(io.getWaterLevel() < currentMessage.getValue()) {
                        		io.fill(true);
                        	} else {
                        		io.fill(false);
                        	}
                            if(!acked && io.getWaterLevel() >= currentMessage.getValue()) {
                            	currentMessage.getSender().send(new WashingMessage(this, WashingMessage.ACKNOWLEDGMENT));
                            	acked = true;
                            }
                            break;

                        case WashingMessage.WATER_IDLE:
                        	io.fill(false);
                        	io.drain(false);
                            break;
                    }
                }
            }
        } catch (InterruptedException unexpected) {
            // we don't expect this thread to be interrupted,
            // so throw an error if it happens anyway
            throw new Error(unexpected);
        }
    }
}
