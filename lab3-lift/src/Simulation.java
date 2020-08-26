import lift.LiftView;

public class Simulation {

	private static final int NBR_PERSONS = 20;

	public static void main(String[] args) {
		LiftView view = new LiftView();
		
		Person[] persons = new Person[NBR_PERSONS];

        for(int i = 0; i < 20; i++) {
        	persons[i] = new Person(view.createPassenger());
        	persons[i].start();
        }
        
        Monitor monitor = new Monitor(persons);
        Lift lift = new Lift(monitor, view);
        
        lift.start();

        /*
        int from = passenger.getStartFloor();
        int to   = passenger.getDestinationFloor();

        passenger.begin();              // walk in (from left)
        if (from != 0) {
            view.moveLift(0, from);
        }
        passenger.enterLift();          // step inside
        view.moveLift(from, to);
        passenger.exitLift();           // leave lift
        passenger.end();                // walk out (to the right)
		*/
	}

}
