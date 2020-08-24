package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.MicroServicesCounter;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
/**
 * Logistic service in charge of delivering books that have been purchased to customers.
 * Handles {@link DeliveryEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */

public class LogisticsService extends MicroService {
	//-----------------------------------------fields----------------------------------------------------------------------------
	private int currentTick;
	//--------------------------------------------------constractor------------------------------------------------------------------
	public LogisticsService(int i) {
		super("LogisticsService"+i);
		this.currentTick=0;
	}
	//-------------------------------------------------------methods------------------------------------------------------------------
	@Override
	//creates the micro service
	protected void initialize() {
		//this callback get a car from the resource micro service , deliver the book to the customer and brings it back
		//to the resource holder by the resource service


		this.subscribeEvent(DeliveryEvent.class,ev -> {
			CarAcquire checkEvent = new CarAcquire();
			Future<Future<DeliveryVehicle>> futureOfDeliveryVehicleFuture = sendEvent(checkEvent);
			if (futureOfDeliveryVehicleFuture == null) {

			}
			else {
				Future<DeliveryVehicle> DeliveryVehicleFuture = futureOfDeliveryVehicleFuture.get();
				if (DeliveryVehicleFuture == null) {


				}
				else {
					DeliveryVehicle deliveryVehicle = futureOfDeliveryVehicleFuture.get().get();


					if (deliveryVehicle != null) {
						deliveryVehicle.deliver(ev.getC().getAddress(), ev.getC().getDistance());
						CarRelease carRelease = new CarRelease(deliveryVehicle);
						this.sendEvent(carRelease);
						this.complete(ev, null);
					}

				}
			}
		});
		//this callback destroys the thread
		this.subscribeBroadcast(finalTickBroadcast.class, ev -> {
			this.terminate();
		});
		MicroServicesCounter.getInstance().add();  //the current micro service finish initialize , therefore we increament the micro services counter by 1
	}
}
