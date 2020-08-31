package lift;

import java.util.Random;

import lift.Passenger;

public class Person extends Thread {
	private static final long SLEEP_DURATION = 100;
	private Passenger passenger;
	private Monitor monitor;
	private boolean goingUp;

	public Person(Passenger passenger, Monitor monitor) {
		this.passenger = passenger;
		this.monitor = monitor;

		goingUp = passenger.getStartFloor() - passenger.getDestinationFloor() < 0 ? true : false;
	}

	// TODO: Fix exit so that it syncs data with monitor

	@Override
	public void run() {
		try {
			delay(45);
			begin();
			awaitEntry();
			awaitExit();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void delay(int maxDelay) throws InterruptedException {
		Random rand = new Random();
		int delayTime = rand.nextInt(46);
		Thread.sleep(delayTime * 1000);
	}

	private void begin() {
		passenger.begin();
		monitor.addWaitingPerson(this);
	}

	private synchronized void awaitExit() throws InterruptedException {
		monitor.exitWhenAllowed(this);
		passenger.exitLift();
		monitor.completeExiting(this);
		passenger.end();
	}

	private synchronized void awaitEntry() throws InterruptedException {
		monitor.enterWhenAllowed(this);
		passenger.enterLift();
		monitor.completeEntering(this);

		/*
		 * while (!entryAllowed()) { Thread.sleep(SLEEP_DURATION); }
		 */

	}

	private boolean exitAllowed() {
		return !monitor.isMoving() && monitor.getFloor() == passenger.getDestinationFloor();
	}

	// TODO: Check approach with ongoing entry/exit. Problem is that sometimes more
	// than 4 ppl get in lift.
	/*
	 * public boolean entryAllowed() {
	 * 
	 * boolean allowed = !monitor.liftFull() && !monitor.isMoving() &&
	 * monitor.getFloor() == passenger.getStartFloor() && monitor.goingUp() ==
	 * goingUp && !monitor.ongoingEntry();
	 * 
	 * 
	 * return allowed; }
	 */
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
