package clock;

public class TickerThread extends Thread {
	private ClockData clockData;
	private final int THREAD_DELAY = 990;
	private final int TICK_INTERVAL = 1000;
	
	public TickerThread(ClockData clockData) {
		this.clockData = clockData;
	}
	
	@Override
	public void run() {
		long start = System.currentTimeMillis();
		while(true) {
			long now = System.currentTimeMillis();
			long timeDifference = now - start;
			
			//Following check will be encountered multiple times per tick.
			//THREAD_DELAY is set to slightly less than TICK_INTERVAL for increased accuracy.
			//(as opposed to the same value) Causes loop execute a few more iterations per tick.
			if(timeDifference >= TICK_INTERVAL) {
				clockData.clockTick();
				start = now;
				
				//TODO: Handle alarm
				
				try {
					Thread.sleep(THREAD_DELAY);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	

}
