package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.Future;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Passive object representing the resource manager.
 * You must not alter any of the given public methods of this class.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
 public  class  ResourcesHolder   {
	//-----------------------------------------fields----------------------------------------------------------------------------
	private LinkedBlockingQueue<DeliveryVehicle> deliveryVehicles;
	private LinkedBlockingQueue<Future<DeliveryVehicle>> resourceHolderQueueOfFutures;
	private static ResourcesHolder instance=null;
	
	/**
     * Retrieves the single instance of this class.
     */
	//--------------------------------------------------constractor------------------------------------------------------------------
	public  static ResourcesHolder getInstance() {
		if (instance==null)
		{
			instance=new ResourcesHolder();
		}
		return instance;


	}
	private  ResourcesHolder()
	{
		this.deliveryVehicles=new LinkedBlockingQueue<>();
		this.resourceHolderQueueOfFutures =new LinkedBlockingQueue<>();
	}

	
	/**
     * Tries to acquire a vehicle and gives a future object which will
     * resolve to a vehicle.
     * <p>
     * @return 	{@link Future<DeliveryVehicle>} object which will resolve to a 
     * 			{@link DeliveryVehicle} when completed.   
     */
	//-------------------------------------------------------methods------------------------------------------------------------------
	public  Future<DeliveryVehicle> acquireVehicle() {
		Future<DeliveryVehicle> future=new Future<>();
		if(deliveryVehicles.isEmpty())
		{
			//if there isn't available car, the future will be added to the queue and waite for a car release to be solved
			resourceHolderQueueOfFutures.add(future);


		}
		else
		{
			// if there is a car available in the queue, it resolve the future with that vehicle
			 future.resolve(deliveryVehicles.poll());
		}
		return future;



	}
	
	/**
     * Releases a specified vehicle, opening it again for the possibility of
     * acquisition.
     * <p>
     * @param vehicle	{@link DeliveryVehicle} to be released.
     */
	public  void releaseVehicle(DeliveryVehicle vehicle) {
		if (resourceHolderQueueOfFutures.isEmpty())
		{
			// if there is no future which is waiting to a car, the gotten car is added to the deliveryVehicles queue
			deliveryVehicles.add(vehicle);
		}
		else
		{
			// if there is a future which is waiting for a car, the function resolve that future with that car
			resourceHolderQueueOfFutures.poll().resolve(vehicle);
		}


	}
	
	/**
     * Receives a collection of vehicles and stores them.
     * <p>
     * @param vehicles	Array of {@link DeliveryVehicle} instances to store.
     */
	public  void load(DeliveryVehicle[] vehicles) {
		for (int i=0;i<vehicles.length;i++){
			this.deliveryVehicles.add(vehicles[i]);
		}
	}
}
