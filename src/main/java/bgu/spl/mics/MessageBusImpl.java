package bgu.spl.mics;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	private ConcurrentHashMap<MicroService, ConcurrentLinkedQueue<Message>> microServicesQueues;
	private ConcurrentHashMap<Class<? extends Event> , ConcurrentLinkedQueue<MicroService>> events;
	private ConcurrentHashMap<Class<? extends Broadcast>, Vector<MicroService>> brodcastsHashmap;
	private ConcurrentHashMap<Event<?>,Future> futures;
	private static MessageBusImpl instance=null;
	private Object lockSubscribeEvent;
	private Object lockSubscribeBroadcast;


	private MessageBusImpl()
	{
		microServicesQueues=new ConcurrentHashMap<>();
		events=new ConcurrentHashMap<>();
		brodcastsHashmap =new ConcurrentHashMap<>();
		futures= new ConcurrentHashMap<>();
		this.lockSubscribeBroadcast=new Object();
		this.lockSubscribeEvent=new Object();

	}
	public synchronized static MessageBusImpl getInstance()
	{
		if (instance==null)
		{
			instance=new MessageBusImpl();
		}
		return instance;
	}




	@Override
	public   <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		synchronized (lockSubscribeEvent) {
			if (events.containsKey(type)) {
				events.get(type).add(m);
			} else {
				events.put(type, new ConcurrentLinkedQueue<MicroService>());
				events.get(type).add(m);
			}
		}
	}

	@Override
	public  void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		synchronized (lockSubscribeBroadcast) {
			if (brodcastsHashmap.containsKey(type)) {
				brodcastsHashmap.get(type).add(m);
			} else {
				brodcastsHashmap.put(type, new Vector<MicroService>());
				brodcastsHashmap.get(type).add(m);

			}
		}
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		futures.get(e).resolve(result);

	}

	@Override
	public  void  sendBroadcast(Broadcast b) {
		if((brodcastsHashmap.containsKey(b.getClass()))&&(!(brodcastsHashmap.get(b.getClass()).isEmpty()))) {
			synchronized (brodcastsHashmap.get(b.getClass())){
		for (MicroService temp: brodcastsHashmap.get(b.getClass())) {
			microServicesQueues.get(temp).add(b);
			synchronized (temp) {
				temp.notifyAll();
			}
		}
		}
		}
	}

	@Override
	public    <T> Future<T> sendEvent(Event<T> e) {

		if(!(events.containsKey(e.getClass()))||(events.get(e.getClass()).isEmpty()))
		{
			return null;
		}
		Future<T> future=new Future<T>();
		futures.put(e,future);
		synchronized (events.get(e.getClass())) {
			MicroService temp = events.get(e.getClass()).poll();
				if (microServicesQueues.isEmpty() || temp == null || microServicesQueues.get(temp) == null) {
					return null;
				} else {

					microServicesQueues.get(temp).add(e);
					events.get(e.getClass()).add(temp);
					synchronized (temp) {
						temp.notify();
					}
			//	}
			}
		}

		return future;
	}

	@Override
	public void  register(MicroService m) {

		microServicesQueues.put(m, new ConcurrentLinkedQueue<Message>());
	}

	@Override
	public  void unregister(MicroService m) {

		for(ConcurrentHashMap.Entry<Class<? extends Event>, ConcurrentLinkedQueue<MicroService>> entry : events.entrySet()){
			entry.getValue().remove(m);
		}
		for(ConcurrentHashMap.Entry<Class<? extends Broadcast>, Vector<MicroService>> entry : brodcastsHashmap.entrySet()){
			synchronized (entry) {
				entry.getValue().remove(m);
			}
		}
		while(!microServicesQueues.get(m).isEmpty()){
			if(microServicesQueues.get(m).peek() instanceof Event){
				complete((Event)microServicesQueues.get(m).poll(),null);
			}
			else
				microServicesQueues.get(m).poll();
		}
		microServicesQueues.remove(m);


	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		if(!microServicesQueues.containsKey(m))
		{
			throw new  IllegalStateException("IllegalStateException");
		}
		synchronized (m) {
			try {

				while (microServicesQueues.get(m).isEmpty()) {
					m.wait();
				}
				return microServicesQueues.get(m).poll();
			}
			catch (NullPointerException e)
			{
				 return microServicesQueues.get(m).poll();
			}
		}
	}
}