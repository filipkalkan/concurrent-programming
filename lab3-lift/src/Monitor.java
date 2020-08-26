import java.util.ArrayList;

public class Monitor {
	
	private static final int MAX_LOAD = 4;
	private static final int NBR_FLOORS = 6;
	private int floor; // the floor the lift is currently on
	private boolean moving; // true if the lift is moving, false otherwise
	private boolean goingUp;
	private int[] waitEntry; // number of passengers waiting to enter the lift at the various floors
	private int[] waitExit; // number of passengers (in lift) waiting to leave at the various floors
	private int load; // number of passengers currently in the lift
	private ArrayList<Person> waitingPersons;
	private ArrayList<Person> loadedPersons;
	private boolean ongoingEntry;
	private boolean ongoingExit;
	
	public Monitor() {
		waitingPersons = new ArrayList<>();
		loadedPersons = new ArrayList<>();
		goingUp = false;
		floor = 0;
	}

	public boolean liftFull() {
		return loadedPersons.size() >= MAX_LOAD;
	}
	
	public boolean goingUp() {
		return goingUp;
	}

	public int getFloor() {
		return floor;
	}

	public int getNextFloor() {
		if(floor == 0 || floor == NBR_FLOORS) {
			goingUp = !goingUp;
		}
		if(goingUp) {
			return floor + 1;
		} else {
			return floor - 1;
		}
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}
	
	public void toggleMoving() {
		moving = !moving;
	}

	public boolean isMoving() {
		return moving;
	}
	
	public void addWaitingPerson(Person person) {
		waitingPersons.add(person);
	}

	public boolean passengersWantEnter() {
		for(Person person : waitingPersons) {
			if(person.entryAllowed()) {
				return true;
			}
		}
		return false;
	}

	public boolean liftEmpty() {
		return loadedPersons.size() == 0;
	}

	public boolean passengersWantExit() {
		for(Person person : loadedPersons) {
			if (person.getDestinationFloor() == floor) {
				return true;
			}
		}
		return false;
	}

	public synchronized void enterLift(Person person) {
		waitingPersons.remove(person);
		loadedPersons.add(person);
	}

	public void exitLift(Person person) {
		loadedPersons.remove(person);
	}
	
	public void setOngoingEntry(boolean entering) {
		ongoingEntry = entering;
	}
	
	public void setOngoingExit(boolean exiting) {
		ongoingExit = exiting;
	}
	
	public boolean ongoingEntry() {
		return ongoingEntry;
	}
	
	public boolean ongoingExit() {
		return ongoingExit;
	}
	
}
