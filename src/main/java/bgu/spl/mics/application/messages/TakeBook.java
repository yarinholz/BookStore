package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.OrderResult;

public class TakeBook  implements Event<OrderResult> {
    //-----------------------------------------fields----------------------------------------------------------------------------
    private String bookName;
//--------------------------------------------------constractor------------------------------------------------------------------

    public TakeBook(String bookName){
        this.bookName=bookName;

    }

    //-------------------------------------------------------methods------------------------------------------------------------------
    public String getBookName() {
        return bookName;
    }
}
