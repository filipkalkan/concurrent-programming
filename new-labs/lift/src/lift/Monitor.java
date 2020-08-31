package lift;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Monitor extends Thread {
    private static final int MAX_LOAD = 4;
    private static final int NBR_FLOORS = 6;
    
    int floor; // the floor the lift is currently on
    boolean moving; // true if the lift is moving, false if standing still with doors open
    boolean goingUp; // +1 if lift is going up, -1 if going down
    ArrayList<Person> waitingPersons = new ArrayList<>(); // number of passengers waiting to enter the lift at the various floors
    ArrayList<Person> loadedPersons = new ArrayList<>(); // number of passengers (in lift) waiting to leave at the various floors
    int load; // number of passengers currently in the lift
    int entering = 0; // Number of people entering the lift right now
    int exiting = 0;
    boolean doorsOpen = false;

    Semaphore mutex = new Semaphore(1);

	public boolean liftFull() {
        return load >= MAX_LOAD;
    }
	
	private boolean soonFull() {
		return load + entering >= MAX_LOAD;
	}

    public boolean goingUp() {
        return goingUp;
    }

    public int getFloor() {
        return floor;
    }

    public int getNextFloor() {
        if (floor == 0 || floor == NBR_FLOORS) {
            goingUp = !goingUp;
        }
        if (goingUp) {
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

    public boolean passengersWantEnter() {
        for (Person person : waitingPersons) {
            if (personCanEnter(person) || entering > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean liftEmpty() {
        return load == 0;
    }

     public boolean passengersWantExit() {
         for (Person person : loadedPersons) {
             if (personCanExit(person) || exiting > 0) {
                 return true;
             }
         }
         return false;
     }

     public synchronized void enterLift(Person person) {
         loadedPersons.add(person);
     }

/*
    public void enterWhenCan(int destination) throws InterruptedException {
        mutex.acquire();
        load++;
        waitExit[destination]++;
    }
*/    
    public void addWaitingPerson(Person person) {
    	waitingPersons.add(person);
    }

    
    public synchronized void enterWhenAllowed(Person person) throws InterruptedException {
    	while(!personCanEnter(person)) {
    		wait(100);
    	}
    	enterLift(person);
        
        entering++;
    }
    
    public synchronized void completeEntering(Person person) {
    	waitingPersons.remove(person);
        entering--;
        load++;
    }
    
    public synchronized void exitWhenAllowed(Person person) throws InterruptedException {
    	while(!personCanExit(person)) {
    		wait(100);
    	}
        
        exiting++;
    }
    
    public synchronized void completeExiting(Person person) {
    	loadedPersons.remove(person);
        exiting--;
        load--;
    }
    
    public boolean personCanEnter(Person person) {
    	boolean directionCondition;
    	
    	if(person.getStartFloor() == 0 || person.getStartFloor() == 6) {
    		directionCondition = true;
    	} else {
    		directionCondition = this.goingUp == person.isGoingUp();
    	}
    	
    	return !liftFull() &&  getFloor() == person.getStartFloor() && doorsOpen && directionCondition && !soonFull();
    }
    
    public boolean personCanExit(Person person) {
    	return  getFloor() == person.getDestinationFloor() && doorsOpen;
    }
    
    public void setDoorsOpen(boolean open) {
    	doorsOpen = open;
    }

}