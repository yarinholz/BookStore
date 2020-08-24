package bgu.spl.mics.application.passiveObjects;

import java.io.Serializable;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a information about a certain book in the inventory.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class BookInventoryInfo  implements Serializable {
	//-----------------------------------------fields----------------------------------------------------------------------------
	private String bookTitle;
	private AtomicInteger amount;
	private int price;
	//--------------------------------------------------constractor------------------------------------------------------------------
	public BookInventoryInfo(String bookTitle,AtomicInteger amountInInventory,int price){
		this.bookTitle=bookTitle;
		this.amount=amountInInventory;
		this.price=price;
	}


	/**
     * Retrieves the title of this book.
     * <p>
     * @return The title of this book.   
     */
	//get the name of the book
	public String getBookTitle() {

		return  this.bookTitle;
	}

	/**
     * Retrieves the amount of books of this type in the inventory.
     * <p>
     * @return amount of available books.      
     */
	//get the amount in the inventory of the book
	public int getAmountInInventory() {

		return this.amount.get();
	}

	/**
     * Retrieves the price for  book.
     * <p>
     * @return the price of the book.
     */
	//get the price of the book
	public int getPrice() {
		return this.price;
	}
	//reduce the amount in the inventory of the book
	public void reduceAmountInInventory()
	{
		amount.decrementAndGet();

	}


	
	

	
}
