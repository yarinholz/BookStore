package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.MicroServicesCounter;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;
import bgu.spl.mics.application.passiveObjects.OrderResult;

/**
 * Selling service in charge of taking orders from customers.
 * Holds a reference to the {@link MoneyRegister} singleton of the store.
 * Handles {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class SellingService extends MicroService{
	//-----------------------------------------fields----------------------------------------------------------------------------
	int currentTick;
	int orderId;
	//--------------------------------------------------constractor------------------------------------------------------------------
	public SellingService(int i) {
		super("SellingService"+i);
		currentTick=1;
		orderId=1;

	}

	@Override
	//creates the micro service
	protected void initialize() {
//this call back make the order of the asked book to the customer
		//if the order success, it deliver a order to the api service
		this.subscribeEvent(BookOrderEvent.class,ev ->{
			int proccessTick=currentTick;
			CheckAvailability check = new CheckAvailability(ev.getBookName());
			Future<Integer> futurePrice =this.sendEvent(check);
			if(futurePrice==null){
				this.complete(ev,null);
			}
			else{
			 Integer price =futurePrice.get();
			 if(price==null)
			 {
				 this.complete(ev,null);
			 }
			 else if(price==-1)
			 	this.complete(ev,null);
			 else {
				 synchronized (ev.getCustomer()) {
					 if (!(ev.getCustomer().getAvailableCreditAmount() >= price)) {

						 this.complete(ev, null);
					 } else {
						 TakeBook take = new TakeBook(ev.getBookName());
						 Future<OrderResult> OrderFuture = this.sendEvent(take);
						 if (OrderFuture == null)
							 this.complete(ev, null);
						 else {
							 OrderResult done = OrderFuture.get();
							 if (done == null) {
								 this.complete(ev, null);
							 } else if (done == OrderResult.NOT_IN_STOCK) {
								 this.complete(ev, null);
							 } else {
							 	String s ="";
							 	synchronized (s) {
									MoneyRegister.getInstance().chargeCreditCard(ev.getCustomer(), price);
								}
								 OrderReceipt order = new OrderReceipt(orderId, ev.getCustomer().getName(), ev.getCustomer().getId(), ev.getBookName(), price, proccessTick, ev.getOrderTick(), proccessTick);
								 orderId++;
								 MoneyRegister.getInstance().file(order);
								 complete(ev, order);
								 DeliveryEvent deliveryEvent = new DeliveryEvent(ev.getCustomer());
								 sendEvent(deliveryEvent);

							 }
						 }
					 }
				 }

				 }
			 }
		});
		//the call back raise the current tick
		this.subscribeBroadcast(TickBroadcast.class,ev ->{
			this.currentTick++;
		});
		//the call back destroys the thread
		this.subscribeBroadcast(finalTickBroadcast.class, ev -> {
			this.terminate();
		});
		MicroServicesCounter.getInstance().add();  //the current micro service finish initialize , therefore we increament the micro services counter by 1

}
}
