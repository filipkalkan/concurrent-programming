import java.util.Random;

import lift.Passenger;

public class Person extends Thread {
	private Passenger passenger;
	private Random rand;
	private Phase phase;
	
	public static enum Phase{
		BEGINNING,
		WAITING,
		ENTERING,
		ELEVATING,
		EXITING,
		ENDING
	}
	
	public Person(Passenger passenger) {
		this.passenger = passenger;
		this.rand = new Random();
		phase = Phase.BEGINNING;
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				go();
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//TODO: Wait while the conditions arent good. Instead of just waiting once.
	public synchronized void go() throws InterruptedException {
		//wait();
		phase = Phase.BEGINNING;
		passenger.begin();
		phase = Phase.WAITING;
		wait();
		phase = Phase.ENTERING;
		passenger.enterLift();
		phase = Phase.ELEVATING;
		wait();
		phase = Phase.EXITING;
		passenger.exitLift();
		phase = Phase.ENDING;
		passenger.end();
	}
	
	public int getDestinationFloor() {
		return passenger.getDestinationFloor();
	}
	
	public int getStartFloor() {
		return passenger.getStartFloor();
	}
	
	public Phase getPhase() {
		return phase;
	}

}
