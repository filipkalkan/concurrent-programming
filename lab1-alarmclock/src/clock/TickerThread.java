package clock;

public class TickerThread extends Thread {
	private ClockData clockData;
	
	public TickerThread(ClockData clockData) {
		this.clockData = clockData;
	}
	
	@Override
	public void run() {
		long t0 = System.currentTimeMillis();
		while(true) {
			long targetTime = t0 + 1000;
			long now = System.currentTimeMillis();
			long offset = now - targetTime;
			long sleepTime = 1000 - offset;
			
			//clockData.displayTime((int) (now - t0));
			
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	

}
