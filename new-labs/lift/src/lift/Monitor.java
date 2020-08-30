package lift;

import java.util.concurrent.Semaphore;

public class Monitor extends Thread {
    private static final int MAX_LOAD = 4;
    private static final int NBR_FLOORS = 6;
    
    int floor; // the floor the lift is currently on
    boolean moving; // true if the lift is moving, false if standing still with doors open
    int direction; // +1 if lift is going up, -1 if going down
    int[] waitEntry; // number of passengers waiting to enter the lift at the various floors
    int[] waitExit; // number of passengers (in lift) waiting to leave at the various floors
    int load; // number of passengers currently in the lift

    Semaphore mutex = new Semaphore(1);

	public boolean liftFull() {
        return load >= MAX_LOAD;
    }

    public boolean goingUp() {
        return direction > 0;
    }

    public int getFloor() {
        return floor;
    }

    // public int getNextFloor() {
    //     if (floor == 0 || floor == NBR_FLOORS) {
    //         goingUp = !goingUp;
    //     }
    //     if (goingUp) {
    //         return floor + 1;
    //     } else {
    //         return floor - 1;
    //     }
    // }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void toggleMoving() {
        moving = !moving;
    }

    public boolean isMoving() {
        return moving;
    }

    // public boolean passengersWantEnter() {
    //     for (Person person : waitingPersons) {
    //         if (person.entryAllowed()) {
    //             return true;
    //         }
    //     }
    //     return false;
    // }

    public boolean liftEmpty() {
        return load == 0;
    }

    // public boolean passengersWantExit() {
    //     for (Person person : loadedPersons) {
    //         if (person.getDestinationFloor() == floor) {
    //             return true;
    //         }
    //     }
    //     return false;
    // }

    // public synchronized void enterLift(Person person) {
    //     waitingPersons.remove(person);
    //     loadedPersons.add(person);
    // }

    // public void exitLift(Person person) {
    //     loadedPersons.remove(person);
    // }

    // public void setOngoingEntry(boolean entering) {
    //     ongoingEntry = entering;
    // }

    // public void setOngoingExit(boolean exiting) {
    //     ongoingExit = exiting;
    // }

    // public boolean ongoingEntry() {
    //     return ongoingEntry;
    // }

    // public boolean ongoingExit() {
    //     return ongoingExit;
    // }

}