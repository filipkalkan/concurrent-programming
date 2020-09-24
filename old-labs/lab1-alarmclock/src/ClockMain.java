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
        clockData.setAlarmTime(155955);

        Semaphore sem = in.getSemaphore();
        
        Thread tickerThread = new TickerThread(clockData);
        
        tickerThread.start();
        
        while (true) {
            sem.acquire();                        // wait for user input

            UserInput userInput = in.getUserInput();
            int choice = userInput.getChoice();
            int value = userInput.getValue();
            
            if(clockData.alarmIsActive()) {
            	clockData.toggleAlarm();
            }
            
            //Input handling
            switch (choice) {
            case ClockInput.CHOICE_SET_TIME:
            	clockData.setTime(value);
            	break;
            case ClockInput.CHOICE_SET_ALARM:
            	clockData.setAlarmTime(value);
            	break;
            case ClockInput.CHOICE_TOGGLE_ALARM:
            	clockData.toggleAlarm();
            	break;
            }
        }
    }

}
