package bgu.spl.mics.application.passiveObjects;


import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 * Passive object representing the store finance management. 
 * It should hold a list of receipts issued by the store.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class MoneyRegister implements Serializable {
	//-----------------------------------------fields----------------------------------------------------------------------------
	private int TotalEarnings;
	private List<OrderReceipt> orderReceipts;
	private static MoneyRegister instance=null;

	/**
     * Retrieves the single instance of this class.
     */
//--------------------------------------------------constractor------------------------------------------------------------------
	private MoneyRegister()
	{
		orderReceipts=new Vector<>();
		TotalEarnings=0;


	}
	public static MoneyRegister getInstance() {
		if (instance==null)
		{
			instance=new MoneyRegister();
		}
		return instance;

	}
	
	/**
     * Saves an order receipt in the money register.
     * <p>   
     * @param r		The receipt to save in the money register.
     */
	//-------------------------------------------------------methods-----------------------------------------------------------------
	public void file (OrderReceipt r) {
		orderReceipts.add(r);
	}
	
	/**
     * Retrieves the current total earnings of the store.  
     */
	public int getTotalEarnings() {
		return this.TotalEarnings;

	}
	
	/**
     * Charges the credit card of the customer a certain amount of money.
     * <p>
     * @param amount 	amount to charge
     */
	public void chargeCreditCard(Customer c, int amount) {
		c.chargeCustomer(amount);
		this.TotalEarnings=this.TotalEarnings+amount;

	}
	
	/**
     * Prints to a file named @filename a serialized object List<OrderReceipt> which holds all the order receipts 
     * currently in the MoneyRegister
     * This method is called by the main method in order to generate the output.. 
     */
	public void printOrderReceipts(String filename) {

		try{
			FileOutputStream temp = new FileOutputStream(filename);
			ObjectOutputStream returnVal = new ObjectOutputStream(temp);
			returnVal.writeObject(orderReceipts);
			temp.close();
			returnVal.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
