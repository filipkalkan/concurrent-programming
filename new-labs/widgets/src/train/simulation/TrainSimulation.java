package train.simulation;

import train.model.Route;
import train.model.Segment;
import train.view.TrainView;

public class TrainSimulation {

    public static void main(String[] args) {

        TrainView view = new TrainView();
        
        Route route = view.loadRoute();
        
        Segment first = route.next();
        
        first.enter();
    }

}
