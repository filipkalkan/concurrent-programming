package lift;

public class Lift extends Thread {
	private static final long WAITING_TIME = 100;
	LiftView view;
	Monitor monitor;
	Person[] persons;

	public Lift(LiftView view, Monitor monitor, Person[] persons) {
		this.view = view;
		this.monitor = monitor;
		this.persons = persons;
		this.view.openDoors(0);
	}

	@Override
	public void run() {
		while (true) {
			updatePassengers();
			if (monitor.personsToServe()) {
				moveToNextFloor();
			}
		}
	}

	private synchronized void updatePassengers() {
		unloadPassengers();
		loadPassengers();
	}

	private synchronized void loadPassengers() {
		while (!monitor.liftFull() && monitor.passengersWantEnter()) {
			try {
				wait(WAITING_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private synchronized void unloadPassengers() {
		while (!monitor.liftEmpty() && monitor.passengersWantExit()) {
			try {
				wait(WAITING_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private void moveToNextFloor() {
		int floor = monitor.getFloor();
		int nextFloor = monitor.getNextFloor();

		monitor.toggleMoving();
		monitor.setDoorsOpen(false); // close doors
		view.closeDoors();
		view.moveLift(floor, nextFloor);
		view.openDoors(nextFloor);
		monitor.setDoorsOpen(true); // open doors
		monitor.toggleMoving();
		monitor.setFloor(nextFloor);

	}
}
