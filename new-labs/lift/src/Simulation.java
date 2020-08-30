
import lift.LiftView;
import lift.Monitor;
import lift.Person;

public class Simulation {
    public static void main(String[] args) {
        LiftView view = new LiftView();
        Monitor monitor = new Monitor();

        for(int i = 0; i < 20; i++) {
            new Person(view.createPassenger(), monitor);
        }
        
        Lift lift = new Lift(view, monitor);
        lift.start();
    }
}
