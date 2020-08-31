
import lift.Lift;
import lift.LiftView;
import lift.Monitor;
import lift.Person;

public class Simulation {
    private static final int NBR_PERSONS = 20;

	public static void main(String[] args) {
        LiftView view = new LiftView();
        Monitor monitor = new Monitor();

        Person[] persons = new Person[NBR_PERSONS];

        for(int i = 0; i < NBR_PERSONS; i++) {
            Person person = new Person(view.createPassenger(), monitor);
            persons[i] = person;
            person.start();
        }
        
        Lift lift = new Lift(view, monitor, persons);
        lift.start();
        
        while(true) {
        	for(int i = 0; i < NBR_PERSONS; i++) {
        		if(!persons[i].isAlive()) {
            		persons[i] = new Person(view.createPassenger(), monitor);
            		persons[i].start();
        		}
        	}
        }
    }
}
