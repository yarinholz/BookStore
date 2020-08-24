package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.Customer;

public class CheckAvailability implements Event<Integer> {
    //-----------------------------------------fields----------------------------------------------------------------------------
    private  String bookTitle;

//--------------------------------------------------constractor------------------------------------------------------------------
    public CheckAvailability(String bookTitle)
    {
        this.bookTitle=bookTitle;

    }

    //-------------------------------------------------------methods------------------------------------------------------------------
    public String getBookTitle() {
        return bookTitle;
    }

}
