import java.util.concurrent.Semaphore;

import clock.ClockData;
import clock.ClockInput;
import clock.ClockInput.UserInput;
import clock.ClockOutput;
import clock.TickerThread;

public class ClockMain {

    public static void main(String[] args) throws InterruptedException {
        ClockData clockData = new ClockData();

        ClockInput  in  = clockData.getInput();
        ClockOutput out = clockData.getOutput();

        clockData.setTime(155950);

        Semaphore sem = in.getSemaphore();
        
        Thread tickerThread = new TickerThread(clockData);
        
        tickerThread.start();
        
        while (true) {
            sem.acquire();                        // wait for user input

            UserInput userInput = in.getUserInput();
            int choice = userInput.getChoice();
            int value = userInput.getValue();
            
            //TODO: Handle input

            System.out.println("choice = " + choice + "  value=" + value);
            sem.release();
        }
    }
}
