package train.simulation;

import java.util.LinkedList;
import java.util.List;

import train.model.Route;
import train.model.Segment;
import train.view.TrainView;

public class Train extends Thread {
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

			Segment first = route.next();
			Segment second = route.next();
			Segment third = route.next();

			segmentQueue.add(first);
			segmentQueue.add(second);
			segmentQueue.add(third);

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
