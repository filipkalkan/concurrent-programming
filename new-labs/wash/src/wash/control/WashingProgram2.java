package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;

/**
 * Program 3 for washing machine. This also serves as an example of how washing
 * programs can be structured.
 * 
 * This short program stops all regulation of temperature and water levels,
 * stops the barrel from spinning, and drains the machine of water.
 * 
 * It can be used after an emergency stop (program 0) or a power failure.
 */
public class WashingProgram2 extends ActorThread<WashingMessage> {

    private WashingIO io;
    private ActorThread<WashingMessage> temp;
    private ActorThread<WashingMessage> water;
    private ActorThread<WashingMessage> spin;
    
    public WashingProgram2(WashingIO io,
                           ActorThread<WashingMessage> temp,
                           ActorThread<WashingMessage> water,
                           ActorThread<WashingMessage> spin) 
    {
        this.io = io;
        this.temp = temp;
        this.water = water;
        this.spin = spin;
    }
    
    @Override
    public void run() {
        try {
            System.out.println("washing program 1 started");
            
         // Lock the hatch
            io.lock(true);
            //Let water into the machine
            System.out.println("setting WATER_FILL...");
            water.send(new WashingMessage(this, WashingMessage.WATER_FILL, 10));
            WashingMessage ack0 = receive();
            System.out.println("washing program 1 got " + ack0);
            //Heat to 40 degrees
            System.out.println("heating to 40 degrees...");
            temp.send(new WashingMessage(this, WashingMessage.TEMP_SET, 40));
            WashingMessage ackTemp = receive();
            System.out.println("washing program 1 got " + ackTemp);
            // Instruct SpinController to rotate barrel slowly, back and forth
            // Expect an acknowledgment in response.
            System.out.println("setting SPIN_SLOW...");
            spin.send(new WashingMessage(this, WashingMessage.SPIN_SLOW));
            WashingMessage ack1 = receive();
            System.out.println("washing program 1 got " + ack1);
            // Spin for 20 simulated minutes (one minute == 60000 milliseconds)
            Thread.sleep(20 * 60000 / Settings.SPEEDUP);
            //Heat to 60 degrees
            System.out.println("heating to 60 degrees...");
            temp.send(new WashingMessage(this, WashingMessage.TEMP_SET, 60));
            WashingMessage ackTemp1 = receive();
            System.out.println("washing program 1 got " + ackTemp1);
            // Spin for 30 simulated minutes (one minute == 60000 milliseconds)
            Thread.sleep(30 * 60000 / Settings.SPEEDUP);
            //drain, rinse 5 times 2 minutes in cold water,
            temp.send(new WashingMessage(this, WashingMessage.TEMP_IDLE));
            water.send(new WashingMessage(this, WashingMessage.WATER_DRAIN));
            for(int i = 0; i < 5; i++) {
            	water.send(new WashingMessage(this, WashingMessage.WATER_FILL, 10));
            	receive();
            	sleep(2 * 60 * 1000 / Settings.SPEEDUP);
            	water.send(new WashingMessage(this, WashingMessage.WATER_DRAIN));
            	receive();
            }
            //centrifuge for 5 minutes
            spin.send(new WashingMessage(this, WashingMessage.SPIN_SLOW));
            receive();
            sleep(5 * 60 * 1000 / Settings.SPEEDUP);
            // Instruct SpinController to stop spin barrel spin.
            // Expect an acknowledgment in response.
            System.out.println("setting SPIN_OFF...");
            spin.send(new WashingMessage(this, WashingMessage.SPIN_OFF));
            WashingMessage ack2 = receive();
            System.out.println("washing program 1 got " + ack2);
            // Now that the barrel has stopped, it is safe to open the hatch.
            io.lock(false);
            
            System.out.println("washing program 1 finished");
        } catch (InterruptedException e) {
            
            // If we end up here, it means the program was interrupt()'ed:
            // set all controllers to idle

            temp.send(new WashingMessage(this, WashingMessage.TEMP_IDLE));
            water.send(new WashingMessage(this, WashingMessage.WATER_IDLE));
            spin.send(new WashingMessage(this, WashingMessage.SPIN_OFF));
            System.out.println("washing program terminated");
        }
    }
}
