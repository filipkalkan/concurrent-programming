package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;

public class WashingProgram0 extends ActorThread<WashingMessage> {

    private WashingIO io;
    private ActorThread<WashingMessage> temp;
    private ActorThread<WashingMessage> water;
    private ActorThread<WashingMessage> spin;
    
    public WashingProgram0(WashingIO io,
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
        temp.send(new WashingMessage(this, WashingMessage.TEMP_IDLE));
		water.send(new WashingMessage(this, WashingMessage.WATER_IDLE));
		spin.send(new WashingMessage(this, WashingMessage.SPIN_OFF));
		System.out.println("washing program terminated");
    }
}