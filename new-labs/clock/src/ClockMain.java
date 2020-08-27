import java.util.concurrent.Semaphore;
import clock.io.ClockInput;
import clock.io.ClockInput.UserInput;
import clock.io.ClockOutput;
import clock.io.ClockData;
import clock.io.TickerThread;
 
public class ClockMain {
 
    public static void main(String[] args) throws InterruptedException {
        // AlarmClockEmulator emulator = new AlarmClockEmulator();
        ClockData clockData = new ClockData();
 
        ClockInput  in  = clockData.getInput();
        ClockOutput out = clockData.getOutput();
 
        clockData.setTime(15,59,50);
        clockData.setAlarmTime(15,59,55);
 
        Semaphore sem = in.getSemaphore();
 
        Thread tickerThread = new TickerThread(clockData);
 
        tickerThread.start();
 
        while (true) {
            sem.acquire(); // wait for user input
 
            UserInput userInput = in.getUserInput();
            int choice = userInput.getChoice();
            int h = userInput.getHours();
            int m = userInput.getMinutes();
            int s = userInput.getSeconds();
 
            if (clockData.alarmIsActive()) {
                clockData.toggleAlarm();
            }
 
            // Input handling
            switch (choice) {
                case ClockInput.CHOICE_SET_TIME:
                    clockData.setTime(h, m, s);
                    break;
                case ClockInput.CHOICE_SET_ALARM:
                    clockData.setAlarmTime(h, m, s);
                    break;
                case ClockInput.CHOICE_TOGGLE_ALARM:
                    clockData.toggleAlarm();
                    break;
            }
 
            // System.out.println("choice=" + choice + " h=" + h + " m=" + m + " s=" + s);
        }
    }
}