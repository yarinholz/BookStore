package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;

public class CarRelease implements Event {
    //-----------------------------------------fields----------------------------------------------------------------------------
    private DeliveryVehicle deliveryVehicle;
//--------------------------------------------------constractor------------------------------------------------------------------
    public CarRelease(DeliveryVehicle deliveryVehicle) {
        this.deliveryVehicle = deliveryVehicle;
    }

    //-------------------------------------------------------methods------------------------------------------------------------------
    public DeliveryVehicle getDeliveryVehicle() {
        return deliveryVehicle;
    }
}
