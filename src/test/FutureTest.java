package bgu.spl.mics;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class FutureTest {
    private  Future<Integer> myfuture;

    @Before
    public void setUp() throws Exception {
        myfuture=new Future();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void get() {
        myfuture.resolve(6);
        assertEquals(myfuture.isDone(),true);
        assertEquals((int)myfuture.get(),6);

    }

    @Test
    public void resolve() {
        myfuture.resolve(6);
        assertEquals((int)myfuture.get(),6);
        assertEquals(myfuture.isDone(),true);

    }

    @Test
    public void isDone() {
        assertEquals(myfuture.isDone(),false);
        myfuture.resolve(6);
        assertEquals(myfuture.isDone(),true);
    }

    @Test
    public void get1() {
        assertEquals(myfuture.isDone(),null);
        assertEquals((int)myfuture.get(1000, TimeUnit.NANOSECONDS),null);
        myfuture.resolve(6);
        assertEquals((int)myfuture.get(1000, TimeUnit.NANOSECONDS),6);

    }
}