package lab;

import wash.WashingIO;

/**
 * Program 3 for washing machine.
 * Serves as an example of how washing programs are structured.
 * 
 * This short program stops all regulation of temperature and water
 * levels, stops the barrel from spinning, and drains the machine
 * of water.
 * 
 * It is can be used after an emergency stop (program 0) or a
 * power failure.
 */
class WashingProgram3 extends MessagingThread<WashingMessage> {

    private WashingIO io;
    private MessagingThread<WashingMessage> temp;
    private MessagingThread<WashingMessage> water;
    private MessagingThread<WashingMessage> spin;
    
    public WashingProgram3(WashingIO io,
                           MessagingThread<WashingMessage> temp,
                           MessagingThread<WashingMessage> water,
                           MessagingThread<WashingMessage> spin) {
        this.io = io;
        this.temp = temp;
        this.water = water;
        this.spin = spin;
    }
    
    @Override
    public void run() {
        try {
            System.out.println("washing program 3 started");
            
            // Switch off heating
            temp.send(new WashingMessage(this, WashingMessage.TEMP_IDLE));

            // Switch off spin
            spin.send(new WashingMessage(this, WashingMessage.SPIN_OFF));

            // Drain barrel (may take some time)
            water.send(new WashingMessage(this, WashingMessage.WATER_DRAIN));
            WashingMessage ack = receive();  // wait for acknowledgment
            System.out.println("got " + ack);
            water.send(new WashingMessage(this, WashingMessage.WATER_IDLE));

            // Unlock hatch
            io.lock(false);
            
            System.out.println("washing program 3 finished");
        } catch (InterruptedException e) {
            
            // if we end up here, it means the program was interrupt()'ed
            // set all controllers to idle

            temp.send(new WashingMessage(this, WashingMessage.TEMP_IDLE));
            water.send(new WashingMessage(this, WashingMessage.WATER_IDLE));
            spin.send(new WashingMessage(this, WashingMessage.SPIN_OFF));
            System.out.println("washing program terminated");
        }
    }
}
