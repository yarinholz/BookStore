package bgu.spl.mics.application.passiveObjects;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * Passive data-object representing a customer of the store.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Customer implements Serializable {
	//-----------------------------------------fields----------------------------------------------------------------------------
	private String name;
	private  int id;
	private String address;
	private int distance;
	private  List<OrderReceipt> CustomerReceiptList;
	private creditCard creditCard;
	private Vector<order> orderSchedule;
	//--------------------------------------------------constractor------------------------------------------------------------------
	public Customer(String name, int id, String address, int distance, int creditNumber) {
		this.name = name;
		this.id = id;
		this.address = address;
		this.distance = distance;
		CustomerReceiptList = new LinkedList<>();

	}


	/**
     * Retrieves the name of the customer.
     */
	public String getName() {
		return this.name;

	}

	/**
     * Retrieves the ID of the customer  . 
     */
	public int getId() {
		return this.id;

	}
	
	/**
     * Retrieves the address of the customer.  
     */
	public String getAddress() {
		return this.address;

	}
	
	/**
     * Retrieves the distance of the customer from the store.  
     */
	public int getDistance() {
		return this.distance;

	}

	
	/**
     * Retrieves a list of receipts for the purchases this customer has made.
     * <p>
     * @return A list of receipts.
     */
	public List<OrderReceipt> getCustomerReceiptList() {
		return this.CustomerReceiptList;

	}
	
	/**
     * Retrieves the amount of money left on this customers credit card.
     * <p>
     * @return Amount of money left.   
     */
	public int getAvailableCreditAmount() {
		return this.creditCard.getAmount();

	}
	/**
     * Retrieves this customers credit card serial number.
	 * <p>
	 * @return credit card serial number.
     */
	public int getCreditNumber() {
		return this.creditCard.getNumber();

	}
	/**
	 * charge the customer credit card with the gotten amount.
	 * <p>
	 * @param amount to charge the customer.
	 */
	public void  chargeCustomer(int amount)
	{
		this.creditCard.setAmount(this.creditCard.getAmount()-amount);
	}

	/**
	 * return the order schedule.
	 * <p>
	 * @return  Vector of orders.
	 */
	public Vector<order> getOrderSchedule() {
		return orderSchedule;
	}

	/**
	 * set the customers receiptList as new linked list (like constructor)
	 */
	public void setCustomerReceiptList (){
		this.CustomerReceiptList=new LinkedList<OrderReceipt>();
	}

}
