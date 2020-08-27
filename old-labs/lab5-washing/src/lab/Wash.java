package lab;
import simulator.WashingSimulator;
import wash.WashingIO;

public class Wash {

    // simulation speed-up factor:
    // 50 means the simulation is 50 times faster than real time
    public static final int SPEEDUP = 50;

    public static void main(String[] args) throws InterruptedException {
        WashingSimulator sim = new WashingSimulator(SPEEDUP);
        
        WashingIO io = sim.startSimulation();

        TemperatureController temp = new TemperatureController(io);
        WaterController water = new WaterController(io);
        SpinController spin = new SpinController(io);

        temp.start();
        water.start();
        spin.start();

        while (true) {
            int n = io.awaitButton();
            System.out.println("user selected program " + n);

            // TODO:
            // if the user presses buttons 1-3, start a washing program
            // if the user presses button 0, and a program has been started, stop it
        }
    }
};
