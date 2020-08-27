
import lift.LiftView;
import lift.Passenger;

public class OnePersonRidesLift {

    public static void main(String[] args) {

        LiftView view = new LiftView();

        Passenger passenger = view.createPassenger();

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
    }
}
