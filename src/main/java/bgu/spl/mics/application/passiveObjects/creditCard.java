package bgu.spl.mics.application.passiveObjects;

import java.io.Serializable;

public class creditCard implements Serializable {
    //-----------------------------------------fields----------------------------------------------------------------------------
    private int number;
    private int amount;
    //--------------------------------------------------constractor------------------------------------------------------------------
    public creditCard(int number, int amount) {
        this.number = number;
        this.amount = amount;
    }
    //-------------------------------------------------------methods------------------------------------------------------------------
    //get the number of the credit card
    /**
     * Retrieves the number of credit card.
     * <p>
     * @return the number of credit card.
     */
    public int getNumber() {
        return number;
    }
    /**
     *set the number of the credit card
     * @param number   the number of credit card.
     */
    public void setNumber(int number) {
        this.number = number;
    }

    // get the available amount in this credit card
    /**
     * Retrieves amount left in credit card.
     * <p>
     * @return the amount left in credit card
     */
    public int getAmount() {
        return amount;
    }


    /**
     *set the amount of the credit card
     * @param amount   the amount of credit card.
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
