import java.util.ArrayList;

import lift.LiftView;

public class Monitor {

	private static final int LIFT_CAPACITY = 4;
	private int floor; // the floor the lift is currently on
	private boolean moving; // true if the lift is moving, false otherwise
	private boolean goingUp;
	private int[] waitEntry; // number of passengers waiting to enter the lift at the various floors
	private int[] waitExit; // number of passengers (in lift) waiting to leave at the various floors
	private int load; // number of passengers currently in the lift
	private Person[] persons;
	private ArrayList<Person> loadedPersons;
	
	
	private int NBR_FLOORS = 6;
	
	public Monitor(Person[] persons) {
		floor = 0;
		moving = false;
		goingUp = true;
		waitEntry = new int[NBR_FLOORS];
		waitExit = new int[NBR_FLOORS];
		load = 0;
		this.persons = persons;
		loadedPersons = new ArrayList<>();
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
	
	public synchronized void setFloor(int floor) {
		this.floor = floor;
		if (floor == NBR_FLOORS || floor == 0) {
			goingUp = !goingUp;
		}
	}
	
	public synchronized void unloadPassengers() {
		if(!loadedPersons.isEmpty()) {
			for(Person person : loadedPersons) {
				if (person.getDestinationFloor() == floor && person.getPhase() == Person.Phase.ELEVATING) {
					//Client-side syncing is used since monitor cannot notify or wait() a thread without being it's owner/creator. Ownership claimed though sync.
					synchronized (person) {
						person.notify();
					}
				}
			}	
		}
	}
	

	public synchronized void loadPassengers() {
		for(Person person : persons) {
			if (person.getStartFloor() == floor && person.getPhase() == Person.Phase.WAITING) {
				synchronized (person) {
					person.notify();	
				}
			}
		}
	}
}
