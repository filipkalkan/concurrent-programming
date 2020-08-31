
import lift.Lift;
import lift.LiftView;
import lift.Monitor;
import lift.Person;

public class Simulation {
    public static void main(String[] args) {
        LiftView view = new LiftView();
        Monitor monitor = new Monitor();

        Person[] persons = new Person[20];

        for(int i = 0; i < 20; i++) {
            Person person = new Person(view.createPassenger(), monitor);
            persons[i] = person;
            person.start();
        }
        
        Lift lift = new Lift(view, monitor, persons);
        lift.start();
        
        while(true) {
        	for(int i = 0; i < 20; i++) {
        		if(!persons[i].isAlive()) {
            		persons[i] = new Person(view.createPassenger(), monitor);
            		persons[i].start();
        		}
        	}
        }
    }
}
