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

	public TemperatureController(WashingIO io) {
		this.io = io;
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
				}
				switch (command) {
				case WashingMessage.TEMP_IDLE:
					io.heat(false);
					break;
				case WashingMessage.TEMP_SET:
					targetTemp = currentMessage.getValue();
					if (io.getTemperature() < targetTemp - mu) {
						io.heat(true);
					} else {
						io.heat(false);
					}
					break;
				}
				if(currentMessage != null) {
					currentMessage.getSender().send(new WashingMessage(this, WashingMessage.ACKNOWLEDGMENT));
				}
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
