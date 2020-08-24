package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.MicroServicesCounter;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.OrderResult;

/**
 * InventoryService is in charge of the book inventory and stock.
 * Holds a reference to the {@link Inventory} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */

public class InventoryService extends MicroService{
	//-----------------------------------------fields----------------------------------------------------------------------------
	private int counter=0;
	//--------------------------------------------------constractor------------------------------------------------------------------
	public InventoryService(int i) {
		super("InventoryService"+i);

	}
	//-------------------------------------------------------methods------------------------------------------------------------------
	@Override
	//creates the micro service
	protected void initialize() {
		// the callback checks if there is copy of the book they asked to check
		//if there is, it resolves the future with the price, else it resolves with -1
		this.subscribeEvent(CheckAvailability.class, ev ->
		{
			Integer price = Inventory.getInstance().checkAvailabiltyAndGetPrice(ev.getBookTitle());
			this.complete(ev, price);
		});
		//this callback take the book if it possible
		this.subscribeEvent(TakeBook.class, ev -> {
			complete(ev, Inventory.getInstance().take(ev.getBookName()));
		});
		//this callback destroys the thread
		this.subscribeBroadcast(finalTickBroadcast.class, ev -> {
			this.terminate();
		});
		MicroServicesCounter.getInstance().add();  //the current micro service finish initialize , therefore we increament the micro services counter by 1

	}


	}
		

