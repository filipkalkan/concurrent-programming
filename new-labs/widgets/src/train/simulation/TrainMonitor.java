package train.simulation;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import train.model.Segment;

public class TrainMonitor {
	private static final long WAITING_TIME = 100;
	Set<Segment> occupiedSegments = new HashSet<>();
	Lock mutex = new ReentrantLock();
	
	public TrainMonitor() {
		
	}
	
	public synchronized void enterSegment(Segment s) throws InterruptedException {
		while(occupiedSegments.contains(s)) {
			wait(WAITING_TIME);
		}
		mutex.lock();
		occupiedSegments.add(s);
		mutex.unlock();
		s.enter();
	}

	public void exitSegment(Segment s) {
		mutex.lock();
		occupiedSegments.remove(s);
		mutex.unlock();
		s.exit();
	}
	
	

}
