package bgu.spl.mics.application.services;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;
import bgu.spl.mics.application.passiveObjects.order;


import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * APIService is in charge of the connection between a client and the store.
 * It informs the store about desired purchases using {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
//-----------------------------------------fields----------------------------------------------------------------------------
public class APIService extends MicroService{
	private ConcurrentLinkedQueue<Future<OrderReceipt>> futureReceiptsQueue;
	private Customer c;
	private List<Pair<String,Integer>> list;
	private int currentTick;
	public APIService(Customer c,int i) {
		super("APIService"+i);
		this.c=c;
		this.futureReceiptsQueue =new ConcurrentLinkedQueue<>();
		this.currentTick=0;
	}
	//-------------------------------------------------------methods------------------------------------------------------------------
	@Override
	//creates the micro service
	protected void initialize() {
		//the callback raise the current tick and check for book orders in the current tick
		//for every order it get a receipt if it success and gives it  to the customer and get null if it doesn't success
		this.subscribeBroadcast(TickBroadcast.class,ev ->{
			OrderReceipt reseipt;
			currentTick++;
			for(order temp : c.getOrderSchedule()) {
				if (temp.getTick() == currentTick) {
					BookOrderEvent order = new BookOrderEvent((String) temp.getBookTitle(), c, (int) temp.getTick());
					Future<OrderReceipt> OrderFuture = this.sendEvent(order);
					if (OrderFuture == null) {

					} else {
						futureReceiptsQueue.add(OrderFuture);
					}
				}
				while (!futureReceiptsQueue.isEmpty()) {
					if (futureReceiptsQueue.peek() == null) {
						futureReceiptsQueue.poll();
					} else {
						reseipt = futureReceiptsQueue.poll().get();
						if (reseipt != null) {
							c.getCustomerReceiptList().add(reseipt);
						}
					}
				}
			}
		});
		//the callback raise the current tick and check for book orders in the current tick
		//for every order it get a receipt if it success and gives it  to the customer and get null if it doesn't success
		//at the end the function destroys the thread
		this.subscribeBroadcast(finalTickBroadcast.class, ev ->{
			currentTick++;
			OrderReceipt receipt;
			for(order temp : c.getOrderSchedule()) {
				if (temp.getTick() == currentTick) {
					BookOrderEvent order = new BookOrderEvent((String) temp.getBookTitle(), c, (int) temp.getTick());
					Future<OrderReceipt> OrderFuture = this.sendEvent(order);
					if (OrderFuture == null) {

					} else {
						futureReceiptsQueue.add(OrderFuture);
					}
				}
				}
				while (!futureReceiptsQueue.isEmpty()) {
					if (futureReceiptsQueue.peek() == null) {
						futureReceiptsQueue.poll();

					} else {
						receipt = futureReceiptsQueue.poll().get();
						if (receipt != null) {
							c.getCustomerReceiptList().add(receipt);
						}
					}
				}



			this.terminate();
		});
		MicroServicesCounter.getInstance().add(); //the current micro service finish initialize , therefore we increament the micro services counter by 1
	}

}
