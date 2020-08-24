package bgu.spl.mics.application.services;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.finalTickBroadcast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link Tick Broadcast}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService {
	//-----------------------------------------fields----------------------------------------------------------------------------
	private int speed;
	private int duration;
	private int currentTick;
	private int currTime;
	//--------------------------------------------------constractor------------------------------------------------------------------
	public TimeService(int speed, int duration) {
		super("TimeService");
		currTime=0;
		this.speed = speed;
		this.duration = duration;
		this.currentTick = 1;

	}
	//-------------------------------------------------------methods------------------------------------------------------------------
	@Override
	//creates the micro service
	protected void initialize() {
		//this call destroys the thread
		this.subscribeBroadcast(finalTickBroadcast.class, ev -> {
			this.terminate();
		});
		// here we use the timer of java for sending tick broadcasts
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (currentTick > duration - 1) {
					sendBroadcast(new finalTickBroadcast());
					timer.cancel();
					timer.purge();
				} else {
					sendBroadcast(new TickBroadcast());
					currentTick++;
				}
			}
		};

			timer.schedule(task,0,speed);



	}
	public int getSpeed(){
		return this.speed;
	}
	public int getDuration(){
		return this.duration;
	}
}

















