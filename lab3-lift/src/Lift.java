import lift.LiftView;

public class Lift extends Thread {
	private static final long WAITING_TIME = 100;
	LiftView view;
	Monitor monitor;
	
	public Lift(LiftView view, Monitor monitor) {
		this.view = view;
		this.monitor = monitor;
	}
	
	@Override
	public void run() {
		while(true) {
			updatePassengers();
			moveToNextFloor();
		}
	}

	private synchronized void updatePassengers() {
		unloadPassengers();
		loadPassengers();
	}

	private synchronized void loadPassengers() {
		while(!monitor.liftFull() && monitor.passengersWantEnter()) {
			try {
				wait(WAITING_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	private synchronized void unloadPassengers() {
		while(!monitor.liftEmpty() && monitor.passengersWantExit()) {
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
		view.moveLift(floor, nextFloor);
		monitor.toggleMoving();
		monitor.setFloor(nextFloor);
		
	}
}
