package bgu.spl.mics.application.passiveObjects;

import java.io.Serializable;

/**
 * Passive data-object representing a receipt that should 
 * be sent to a customer after the completion of a BookOrderEvent.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class OrderReceipt implements Serializable {
	//-----------------------------------------fields----------------------------------------------------------------------------
	private int OrderId;
	private String seller;
	private  int CustomerId;
	private String BookTitle;
	private int Price;
	private int IssuedTick;
	private int OrderTick;
	private int ProcessTick;
	//--------------------------------------------------constractor------------------------------------------------------------------
	public OrderReceipt(int orderId, String seller, int customerId, String bookTitle, int price, int issuedTick, int orderTick, int processTick) {
		OrderId = orderId;
		this.seller = seller;
		CustomerId = customerId;
		BookTitle = bookTitle;
		Price = price;
		IssuedTick = issuedTick;
		OrderTick = orderTick;
		ProcessTick = processTick;
	}


	/**
     * Retrieves the orderId of this receipt.
     */
	//-------------------------------------------------------methods------------------------------------------------------------------
	public int getOrderId() {
		return this.OrderId;

	}
	
	/**
     * Retrieves the name of the selling service which handled the order.
     */
	public String getSeller() {
		return this.seller;

	}
	
	/**
     * Retrieves the ID of the customer to which this receipt is issued to.
     * <p>
     * @return the ID of the customer
     */
	public int getCustomerId() {
		return this.CustomerId;

	}
	
	/**
     * Retrieves the name of the book which was bought.
     */
	public String getBookTitle() {
		return this.BookTitle;

	}
	
	/**
     * Retrieves the price the customer paid for the book.
     */
	public int getPrice() {
		return this.Price;

	}
	
	/**
     * Retrieves the tick in which this receipt was issued.
     */
	public int getIssuedTick() {
		return this.IssuedTick;

	}
	
	/**
     * Retrieves the tick in which the customer sent the purchase request.
     */
	public int getOrderTick() {
		return this.OrderTick;

	}
	
	/**
     * Retrieves the tick in which the treating selling service started 
     * processing the order.
     */
	public int getProcessTick() {
		return this.ProcessTick;

	}
}
