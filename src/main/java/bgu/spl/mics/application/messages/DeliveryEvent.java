package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Customer;

public class DeliveryEvent implements Event{
    //-----------------------------------------fields----------------------------------------------------------------------------
    private Customer c;
//--------------------------------------------------constractor------------------------------------------------------------------
    public DeliveryEvent(Customer c) {
        this.c = c;
    }

    //-------------------------------------------------------methods------------------------------------------------------------------
    public Customer getC() {
        return c;
    }
}
