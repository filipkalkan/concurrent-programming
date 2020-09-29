package lift;

public class Lift extends Thread {
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
			monitor.updatePassengers();
			if (monitor.personsToServe()) {
				moveToNextFloor();
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
