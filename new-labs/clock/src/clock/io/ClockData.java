package clock.io;
 
import clock.AlarmClockEmulator;
 
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
 
import clock.io.ClockInput;
import clock.io.ClockOutput;
 
public class ClockData {
    private AlarmClockEmulator emulator;
    private ClockInput in;
    private ClockOutput out;
    private int hAlarm = 0;
    private int mAlarm = 0;
    private int sAlarm = 0;
    private boolean alarmSet;
    private int hTime = 0;
    private int mTime = 0;
    private int sTime = 0;
    private final int MAX_NBR_BEEPS = 20;
    private final Lock mutex;
 
    public ClockData() {
        this.emulator = new AlarmClockEmulator();
        this.in = emulator.getInput();
        this.out = emulator.getOutput();
        this.alarmSet = false;
        mutex = new ReentrantLock();
    }
 
    public ClockInput getInput() {
        return in;
    }
 
    public ClockOutput getOutput() {
        return out;
    }
 
    private int toSeconds(int h, int m, int s) {
        int totSeconds = h * 3600 + m * 60 + s;
        return totSeconds;
    }
    
    private int[] toClockFormat(int seconds) {
		int hh = (int) (seconds / 3600);
		int mm = ((int) (seconds / 60)) % 60;
		int ss = seconds % 60;
		
		int[] hms = {hh, mm, ss};
		
		return hms;
	}
 
    public void setTime(int h, int m, int s) {
        mutex.lock();
        hTime = h;
        mTime = m;
        sTime = s;
        out.displayTime(h, m, s);
        mutex.unlock();
    }
 
    public void clockTick() {
        mutex.lock();
        int totSeconds = toSeconds(hTime, mTime, sTime);
        totSeconds++;
        int[] hms = toClockFormat(totSeconds);
        hTime = hms[0];
        mTime = hms[1];
        sTime = hms[2];
        mutex.unlock();
 
        out.displayTime(hTime, mTime, sTime);
    }
 
    public void setAlarmTime(int h, int m, int s) {
        mutex.lock();
        hAlarm = h;
        mAlarm = m;
        sAlarm = s;
        alarmSet = true;
        mutex.unlock();
 
        out.setAlarmIndicator(true);
    }
 
    public void toggleAlarm() {
        mutex.lock();
        alarmSet = !alarmSet;
        mutex.unlock();
        out.setAlarmIndicator(alarmSet);
    }
 
    public boolean alarmIsActive() {
        return alarmSet && toSeconds(hTime, mTime, sTime) >= toSeconds(hAlarm, mAlarm, sAlarm);
        // && toSeconds(alarmTime) + MAX_NBR_BEEPS >= toSeconds(time);
    }
 
//    public int getTime() {
//        return time;
//    }
 
    public void soundAlarm() {
        out.alarm();
        mutex.lock();
        if (toSeconds(hAlarm, mAlarm, sAlarm) + MAX_NBR_BEEPS <= toSeconds(hTime, mTime, sTime)) {
            alarmSet = false;
            out.setAlarmIndicator(false);
        }
        mutex.unlock();
    }
 
}