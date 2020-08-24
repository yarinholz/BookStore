package bgu.spl.mics;

import java.util.concurrent.atomic.AtomicInteger;

public class MicroServicesCounter {
    private static MicroServicesCounter mic=null;
    private AtomicInteger microCounter;

    private MicroServicesCounter()
    {
        microCounter=new AtomicInteger(0);;
    }
    /**
     * Retrieves the single instance of this class.
     */
    public synchronized static MicroServicesCounter getInstance()
    {
        if (mic==null)
        {
            mic=new MicroServicesCounter();
        }
        return mic;
    }
    /**
     * increament by one the microcunter.
     */
    public void add()
    {
        microCounter.incrementAndGet();
    }

    /**
     * Return the counter of the micro services.
     * <p>
     * @return int- the counter of the micro-services.
     */

    public int getMicroCounter() {
        return microCounter.get();
    }
}

