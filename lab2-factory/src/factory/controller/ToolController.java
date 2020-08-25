package factory.controller;

import factory.model.DigitalSignal;
import factory.model.WidgetKind;
import factory.swingview.Factory;

public class ToolController {
    private final DigitalSignal conveyor, press, paint;
    private final long pressingMillis, paintingMillis;
    
    public ToolController(DigitalSignal conveyor,
                          DigitalSignal press,
                          DigitalSignal paint,
                          long pressingMillis,
                          long paintingMillis)
    {
        this.conveyor = conveyor;
        this.press = press;
        this.paint = paint;
        this.pressingMillis = pressingMillis;
        this.paintingMillis = paintingMillis;
    }
    
    //When one thread is executing a synchronized method for an object, all other threads that invoke synchronized methods
    //for the same object block (suspend execution) until the first thread is done with the object

    public synchronized void onPressSensorHigh(WidgetKind widgetKind) throws InterruptedException {
        //
        // TODO: you will need to modify this method
        //
        if (widgetKind == WidgetKind.BLUE_RECTANGULAR_WIDGET) {
            conveyor.off();
            press.on();
            Thread.sleep(pressingMillis);
            press.off();
            Thread.sleep(pressingMillis);   // press needs this time to retract
            conveyor.on();
        }
    }

    public synchronized void onPaintSensorHigh(WidgetKind widgetKind) throws InterruptedException {
        //
        // TODO: you will need to modify this method
        //
    	if (widgetKind == WidgetKind.ORANGE_ROUND_WIDGET) {
        	conveyor.off();
        	paint.on();
        	Thread.sleep(paintingMillis);
        	paint.off();
        	conveyor.on();
        }
    }
    
    // -----------------------------------------------------------------------
    
    public static void main(String[] args) {
        Factory factory = new Factory();
        factory.startSimulation();
    }
}
