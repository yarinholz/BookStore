package bgu.spl.mics.application.passiveObjects;
import java.util.*;
import java.io.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;


public class InventoryTest {
    private Inventory inv;


    @Before
    public void setUp() throws Exception {
        this.getInstance();
        BookInventoryInfo bookA = new BookInventoryInfo("Harry potter", 3, 70);
        BookInventoryInfo bookB = new BookInventoryInfo("melachim g", 1, 120);
        BookInventoryInfo[] books = new BookInventoryInfo[2];
        books[0] = bookA;
        books[1] = bookB;
        inv.load(books);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getInstance() {
    }

    @Test
    public void load() {


                OrderResult or1= inv.take("Harry potter");
                assertEquals(or1, OrderResult.SUCCESSFULLY_TAKEN);
                OrderResult or2= inv.take("Lord of the rings");
                assertEquals(or2, OrderResult.NOT_IN_STOCK);
                int price=inv.checkAvailabiltyAndGetPrice("Harry potter");
                assertEquals(price,70);
                OrderResult or =inv.take("Harry potter");
                OrderResult or3 =inv.take("Harry potter");
                int price2=inv.checkAvailabiltyAndGetPrice("Harry potter");
                assertEquals(price2,-1);


    }

    @Test
    public void take() {
        OrderResult or1= inv.take("melachim g");
        assertEquals(or1, OrderResult.SUCCESSFULLY_TAKEN);
        OrderResult or2= inv.take("melachim g");
        assertEquals(or2, OrderResult.NOT_IN_STOCK);
        OrderResult or3= inv.take("Lord of the rings");
        assertEquals(or3, OrderResult.NOT_IN_STOCK);
    }

    @Test
    public void checkAvailabiltyAndGetPrice() {
        int price=inv.checkAvailabiltyAndGetPrice("Harry potter");
        assertEquals(price,70);
        int price2=inv.checkAvailabiltyAndGetPrice("melachim g");
        assertFalse(price2!=120);
        OrderResult or1= inv.take("melachim g");
        int price3=inv.checkAvailabiltyAndGetPrice("melachim g");
        assertEquals(price3,-1);


    }

    @Test
    public void printInventoryToFile() {


    }
}