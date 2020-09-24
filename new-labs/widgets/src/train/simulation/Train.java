package train.simulation;

import java.util.LinkedList;
import java.util.List;

import train.model.Route;
import train.model.Segment;
import train.view.TrainView;

public class Train extends Thread {
	private static final int TRAIN_LENGTH = 7;
	List<Segment> segmentQueue = new LinkedList<>();
	Route route;
	TrainView view;
	TrainMonitor monitor;

	public Train(TrainView view, TrainMonitor monitor) {
		this.view = view;
		this.monitor = monitor;
	}

	public void run() {
		try {
			route = view.loadRoute();
			
			for(int i = 0; i < TRAIN_LENGTH; i++) {
				Segment s = route.next();
				segmentQueue.add(s);
			}

			for (Segment s : segmentQueue) {
				monitor.enterSegment(s);
			}

			while (true) {
				Segment head = route.next();
				monitor.enterSegment(head);
				segmentQueue.add(head);

				Segment tail = segmentQueue.get(0);
				monitor.exitSegment(tail);
				segmentQueue.remove(0);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
