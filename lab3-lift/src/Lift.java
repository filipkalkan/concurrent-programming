import lift.LiftView;

public class Lift extends Thread {
	
	private Monitor monitor;
	private LiftView view;

	public Lift(Monitor monitor, LiftView view) {
		this.monitor = monitor;
		this.view = view;
	}
	
	@Override
	public void run() {
		while(true) {
			moveLift();
		}
	}

	private void moveLift() {
		int currentFloor = monitor.getCurrentFloor();
		int nextFloor = monitor.getNextFloor();
		view.moveLift(currentFloor, nextFloor);
		monitor.setFloor(nextFloor);
		monitor.unloadPassengers();
		monitor.loadPassengers();
		
	}
}
