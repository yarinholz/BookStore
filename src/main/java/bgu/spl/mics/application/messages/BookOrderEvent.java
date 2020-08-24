package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;



public class BookOrderEvent implements Event<OrderReceipt> {
    //-----------------------------------------fields----------------------------------------------------------------------------
    private String bookName;
    private Customer customer;
    private int orderTick;
//--------------------------------------------------constractor------------------------------------------------------------------
   public BookOrderEvent(String bookName,Customer customer,int orderTick) {

        this.bookName=bookName;
        this.customer=customer;
        this.orderTick=orderTick;
    }

//-------------------------------------------------------methods------------------------------------------------------------------

// get the book name
    public String getBookName() {
        return bookName;
    }
//get the customer that ordered the book
    public Customer getCustomer() {
        return customer;
    }
// get the order tick
    public int getOrderTick() {
        return orderTick;
    }
}
