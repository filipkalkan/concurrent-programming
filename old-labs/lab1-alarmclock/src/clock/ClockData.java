package clock;

import emulator.AlarmClockEmulator;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import clock.ClockInput;
import clock.ClockOutput;

public class ClockData {
	private AlarmClockEmulator emulator;
	private ClockInput in;
	private ClockOutput out;
	private int alarmTime;
	private boolean alarmSet;
	private int time = 0;
	private final int MAX_NBR_BEEPS = 20;
	private final Lock mutex;
	
	
	public ClockData() {
		this.emulator = new AlarmClockEmulator();
		this.in = emulator.getInput();
		this.out = emulator.getOutput();
		this.alarmTime = 0;
		this.alarmSet = false;
		mutex = new ReentrantLock();
	}
	
	public ClockInput getInput() {
		return in;
	}
	
	public ClockOutput getOutput() {
		return out;
	}
	
	public void setTime(int hhmmss) {
		mutex.lock();
		time = hhmmss;
		out.displayTime(hhmmss);
		mutex.unlock();
	}
	
	private int toSeconds(int hhmmss) {
		int hh = (int) hhmmss / 10000;
		int mm = (int) (hhmmss - hh * 10000) / 100;
		int ss = hhmmss - hh * 10000 - mm * 100;
		
		int totSeconds = hh * 3600 + mm * 60 + ss;
		return totSeconds;
	}
	
	private int toClockFormat(int seconds) {
		int hh = (int) (seconds / 3600);
		int mm = ((int) (seconds / 60)) % 60;
		int ss = seconds % 60;
		
		int clockFormat = hh * 10000 + mm * 100 + ss;
		
		return clockFormat;
	}
	
	public void clockTick() {
		mutex.lock();
		int totSeconds = toSeconds(time);
		totSeconds++;
		time = toClockFormat(totSeconds);
		mutex.unlock();
		
		out.displayTime(time);
	}
	
	public void setAlarmTime(int hhmmss) {
		mutex.lock();
		alarmTime = hhmmss;
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
		return alarmSet && toSeconds(time) >= toSeconds(alarmTime);
				//&& toSeconds(alarmTime) + MAX_NBR_BEEPS >= toSeconds(time);
	}
	
	public int getTime() {
		return time;
	}
	
	public void soundAlarm() {
		out.alarm();
		mutex.lock();
		if(toSeconds(alarmTime) + MAX_NBR_BEEPS <= toSeconds(time)) {
			alarmSet = false;
			out.setAlarmIndicator(false);
		}
		mutex.unlock();
	}
	

}
