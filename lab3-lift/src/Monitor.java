import lift.LiftView;

public class Monitor {

	private LiftView liftView;
	private int floor; // the floor the lift is currently on
	private boolean moving; // true if the lift is moving, false otherwise
	private boolean goingUp;
	private int[] waitEntry; // number of passengers waiting to enter the lift at the various floors
	private int[] waitExit; // number of passengers (in lift) waiting to leave at the various floors
	private int load; // number of passengers currently in the lift
	private Person[] persons;
	
	
	private int NBR_FLOORS = 6;
	
	public Monitor(LiftView liftView, Person[] persons) {
		this.liftView = liftView;
		floor = 0;
		moving = false;
		goingUp = true;
		waitEntry = new int[NBR_FLOORS];
		waitExit = new int[NBR_FLOORS];
		load = 0;
		this.persons = persons;
	}
	
	public int getCurrentFloor() {
		return floor;
	}
	
	public int getNextFloor() {
		if (goingUp) {
			return floor + 1;
		} else {
			return floor - 1;
		}
	}
	
	public void setFloor(int floor) {
		this.floor = floor;
		if (floor == NBR_FLOORS || floor == 0) {
			goingUp = !goingUp;
		}
	}
	
	public void unloadPassengers() {
		for(Person person : persons) {
			if (person.getDestinationFloor() == floor) {
				person.notify();
			}
		}
	}
	
	public void loadPassengers() {
		for(Person person : persons) {
			if (person.getStartFloor() == floor) {
				person.notify();
			}
		}
	}
}
