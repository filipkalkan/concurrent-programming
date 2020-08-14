package clock;

import emulator.AlarmClockEmulator;
import clock.ClockInput;
import clock.ClockOutput;

public class ClockData {
	private AlarmClockEmulator emulator;
	private ClockInput in;
	private ClockOutput out;
	private int alarmTime;
	private boolean configuringAlarm;
	private boolean alarmSet;
	private int time = 0;
	
	
	public ClockData() {
		this.emulator = new AlarmClockEmulator();
		this.in = emulator.getInput();
		this.out = emulator.getOutput();
		this.alarmTime = 0;
		this.configuringAlarm = false;
		this.alarmSet = false;
	}
	
	public ClockInput getInput() {
		return in;
	}
	
	public ClockOutput getOutput() {
		return out;
	}
	
	public void setTime(int hhmmss) {
		time = hhmmss;
		out.displayTime(hhmmss);
	}
	
	private int toSeconds(int hhmmss) {
		int hh = (int) time / 10000;
		int mm = (int) (time - hh * 10000) / 100;
		int ss = time - hh * 10000 - mm * 100;
		
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
		int totSeconds = toSeconds(time);
		totSeconds++;
		time = toClockFormat(totSeconds);
		
		out.displayTime(time);
	}
	
	public void setAlarmTime(int hhmmss) {
		alarmTime = hhmmss;
		alarmSet = true;
		out.setAlarmIndicator(true);
	}
	
	public void terminateAlarm() {
		alarmSet = false;
		out.setAlarmIndicator(false);
	}
	
	public boolean alarmIsActive() {
		if(alarmSet && toSeconds(time) >= toSeconds(alarmTime)) {
			return true;
		} else {
			return false;
		}
	}
	
	public int getTime() {
		return time;
	}

}
