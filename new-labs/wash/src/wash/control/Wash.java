package wash.control;
import actor.ActorThread;
import wash.io.WashingIO;
import wash.simulation.WashingSimulator;

public class Wash {

    public static void main(String[] args) throws InterruptedException {
    	//WashingSimulator sim = new WashingSimulator(Settings.SPEEDUP);
    	WashingSimulator sim = new WashingSimulator(Settings.SPEEDUP);
        
        WashingIO io = sim.startSimulation();

        TemperatureController temp = new TemperatureController(io);
        WaterController water = new WaterController(io);
        SpinController spin = new SpinController(io);

        temp.start();
        water.start();
        spin.start();
        
        ActorThread<WashingMessage> runningProgram = null;

        while (true) {
            int n = io.awaitButton();
            System.out.println("user selected program " + n);

            switch(n) {
            case 1: 
            	if(runningProgram == null) {
            		runningProgram = new WashingProgram1(io, temp, water, spin);
            		runningProgram.start();
            	} else {
            		System.out.println("A program is already running!");
            	}
            	break;
            case 2:
            	if(runningProgram == null) {

            	} else {
            		System.out.println("A program is already running!");
            	}
            	break;
            case 3:
            	if(runningProgram == null) {

            		runningProgram = new WashingProgram3(io, temp, water, spin);
            		runningProgram.start();
                    
            	} else {
            		System.out.println("A program is already running!");
            	}
            	break;
            case 0:
            	// Turn off machine
            	runningProgram.interrupt();
            	runningProgram = new WashingProgram0(io, temp, water, spin);
            	runningProgram.start();
            	runningProgram.join();
            	
            	runningProgram = null;
            	break;
            	
            }
            // TODO:
            // if the user presses buttons 1-3, start a washing program
            // if the user presses button 0, and a program has been started, stop it
        }
    }
};
