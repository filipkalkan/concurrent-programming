package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;

public class TemperatureController extends ActorThread<WashingMessage> {
	private long dt = 10000; // ms
	private double mu = (dt / 1000) * 0.0478;
	private double ml = (dt / 1000) * 9.52 * Math.pow(10, -4);
	private WashingIO io;
	private WashingMessage currentMessage;
	private double targetTemp;
	private int command;
	private boolean acked;
	private boolean reachedUpperLastTime;

	public TemperatureController(WashingIO io) {
		this.io = io;
		this.targetTemp = 0;
		this.acked = true;
		this.reachedUpperLastTime = false;
	}

	@Override
	public void run() {

		try {
			while (true) {
				// wait for up to dt for a WashingMessage
				WashingMessage recievedMessage = receiveWithTimeout(dt / Settings.SPEEDUP);
				if (recievedMessage != null) {
					currentMessage = recievedMessage;
					command = recievedMessage.getCommand();
					acked = false;
					targetTemp = currentMessage.getValue();
				}
				switch (command) {
				case WashingMessage.TEMP_IDLE:
					reachedUpperLastTime = false;
					io.heat(false);
					break;
				case WashingMessage.TEMP_SET:
					double currentTemp = io.getTemperature();
					if (currentTemp < targetTemp - mu && reachedUpperLastTime == false) {
						io.heat(true);
					} else if(currentTemp >= targetTemp - mu){
						reachedUpperLastTime = true;
						io.heat(false);
					} else if(currentTemp <= targetTemp - 2 + ml + 0.5) { //Is ml to small?? we shouldn't need +0.5 here
						reachedUpperLastTime = false;
						io.heat(true);
					}
					break;
				}
				if(!acked && (currentMessage.getCommand() == WashingMessage.TEMP_SET ? reachedUpperLastTime : true)) {
					currentMessage.getSender().send(new WashingMessage(this, WashingMessage.ACKNOWLEDGMENT));
					acked = true;
				}
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
