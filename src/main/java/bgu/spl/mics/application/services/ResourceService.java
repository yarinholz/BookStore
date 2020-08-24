package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.MicroServicesCounter;
import bgu.spl.mics.application.messages.CarAcquire;
import bgu.spl.mics.application.messages.CarRelease;
import bgu.spl.mics.application.messages.finalTickBroadcast;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;
import java.util.LinkedList;
import java.util.Queue;

/**
 * ResourceService is in charge of the store resources - the delivery vehicles.
 * Holds a reference to the {@link ResourceHolder} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ResourceService extends MicroService{
	//-----------------------------------------fields----------------------------------------------------------------------------
	private Queue<Future<DeliveryVehicle>> futures;
	//--------------------------------------------------constractor------------------------------------------------------------------

	public ResourceService(int i) {
		super("ResorceService"+i);
		futures=new LinkedList<>();
	}
	//-------------------------------------------------------methods------------------------------------------------------------------
	@Override
	//creates the micro service
	protected void initialize() {
		//this call back get a future of a car and deliver it by future to the logistic service
		this.subscribeEvent(CarAcquire.class, ev ->{
			Future<DeliveryVehicle> future = ResourcesHolder.getInstance().acquireVehicle();
			//add the future to the private future queue of the micro service
			futures.add(future);
			  this.complete(ev,future);
		});
		//this call back get a car from the logistic service and bing it back to the resource holder
		this.subscribeEvent(CarRelease.class, ev ->{
			ResourcesHolder.getInstance().releaseVehicle(ev.getDeliveryVehicle());
			this.complete(ev,null);
		});
		// this call back resolves all the futures that did'nt resolved before the final tick and destroys the thread
		this.subscribeBroadcast(finalTickBroadcast.class, ev -> {
			synchronized (futures) {
				while (!futures.isEmpty()) {
					if (!(futures.peek().isDone()))
						futures.poll().resolve(null);
					else
						futures.poll();
				}
			}
			this.terminate();
		});
		MicroServicesCounter.getInstance().add(); //the current micro service finish initialize , therefore we increament the micro services counter by 1


	}

}
