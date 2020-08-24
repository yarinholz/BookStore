package bgu.spl.mics.application.passiveObjects;

import java.io.Serializable;

public class order implements Serializable {
    //-----------------------------------------fields----------------------------------------------------------------------------
    private String bookTitle;
    private int tick;
    //-------------------------------------------------------methods------------------------------------------------------------------
    public order(String bookTitle, int tick) {
        this.bookTitle = bookTitle;
        this.tick = tick;
    }

    /**
     * get the order tick.
     * <p>
     * @return int tick .
     */
    public int getTick() {
        return tick;
    }


    /**
     * get the name of the ordered book.
     * <p>
     * @return string book title .
     */
    public String getBookTitle() {
        return bookTitle;
    }

}
