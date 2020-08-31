package lift;

import java.util.Random;

import lift.Passenger;

public class Person extends Thread {
	private Passenger passenger;
	private Monitor monitor;
	private boolean goingUp;

	public Person(Passenger passenger, Monitor monitor) {
		this.passenger = passenger;
		this.monitor = monitor;

		goingUp = passenger.getStartFloor() - passenger.getDestinationFloor() < 0 ? true : false;
	}

	@Override
	public void run() {
		try {
			delay(45);
			begin();
			awaitAndEnter();
			awaitAndExit();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void delay(int maxDelaySeconds) throws InterruptedException {
		Random rand = new Random();
		int delayTime = rand.nextInt(maxDelaySeconds);
		Thread.sleep(delayTime * 1000);
	}

	private void begin() {
		passenger.begin();
		monitor.addWaitingPerson(this);
	}

	private synchronized void awaitAndExit() throws InterruptedException {
		monitor.exitWhenAllowed(this);
		passenger.exitLift();
		monitor.completeExiting(this);
		passenger.end();
	}

	private synchronized void awaitAndEnter() throws InterruptedException {
		monitor.enterWhenAllowed(this);
		passenger.enterLift();
		monitor.completeEntering(this);
	}

	public int getStartFloor() {
		return passenger.getStartFloor();
	}

	public int getDestinationFloor() {
		return passenger.getDestinationFloor();
	}

	public boolean isGoingUp() {
		return goingUp;
	}

}
