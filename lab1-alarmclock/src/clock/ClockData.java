package clock;

import emulator.AlarmClockEmulator;
import clock.ClockInput;
import clock.ClockOutput;

public class ClockData {
	private AlarmClockEmulator emulator;
	private ClockInput in;
	private ClockOutput out;
	private int alarmTime;
	private boolean alarmOn;
	private boolean configuringAlarm;
	private boolean alarmSet;
	private int time = 0;
	
	
	public ClockData() {
		this.emulator = new AlarmClockEmulator();
		this.in = emulator.getInput();
		this.out = emulator.getOutput();
		this.alarmTime = 0;
		this.alarmOn = false;
		this.configuringAlarm = false;
		this.alarmSet = false;
	}
	
	public ClockInput getInput() {
		return in;
	}
	
	public ClockOutput getOutput() {
		return out;
	}
	
	public void clockTick() {
		int hh = (int) time / 10000;
		int mm = (int) (time - hh * 10000) / 100;
		int ss = time - hh * 10000 - mm * 100;
		
		int totSeconds = hh * 3600 + mm * 60 + ss;
		totSeconds++;
		
		hh = (int) (totSeconds / 3600);
		mm = ((int) (totSeconds / 60)) % 60;
		ss = totSeconds % 60;
		
		time = hh * 10000 + mm * 100 + ss;
		
		out.displayTime(time);
	}
	
	public void setAlarmTime(int hhmmss) {
		alarmTime = hhmmss;
		alarmSet = true;
	}
	
	public int getTime() {
		return time;
	}

}
