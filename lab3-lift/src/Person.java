import java.util.Random;

import lift.Passenger;

public class Person extends Thread {
	private Passenger passenger;
	private Random rand;
	
	public Person(Passenger passenger) {
		this.passenger = passenger;
		this.rand = new Random();
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				//wait();
				passenger.begin();
				wait();
				passenger.enterLift();
				wait();
				passenger.exitLift();
				passenger.end();
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public int getDestinationFloor() {
		return passenger.getDestinationFloor();
	}
	
	public int getStartFloor() {
		return passenger.getStartFloor();
	}

}
