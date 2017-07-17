/**
 * Created by salman on 17/7/17.
 */
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class LFUTest {
    LFUCache<Integer, Integer> lfu = new LFUCache<>(100);

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
    }

    @Test
    public void TestCase1() {
        lfu.set(5, 5);
        lfu.set(4, 4);
        lfu.set(3, 3);
        lfu.set(4, 4);
        Assert.assertEquals(Integer.valueOf("5"), lfu.get(5) );
        String output = "";
        output = output + "1 : (3, 3) \n" + "2 : (4, 4) (5, 5) \n";
         lfu.printLFU();
         Assert.assertEquals(output, outContent.toString());

         lfu.evict();

         lfu.printLFU();
         output = output + "2 : (4, 4) (5, 5) \n";
         Assert.assertEquals(output, outContent.toString());

         lfu.evict();
         lfu.printLFU();
         output = output + "2 : (5, 5) \n";
         Assert.assertEquals(output, outContent.toString());
    }

    @Test
    public void TestCase2() {
        lfu.evict();
        Assert.assertEquals(null, lfu.get(1) );

        lfu.set(1, 1);
        lfu.printLFU();
        String output = "";
        output = output + "2 : (1, 1) \n";
        Assert.assertEquals(output, outContent.toString());

        lfu.set(2, 2);
        lfu.printLFU();
        output = output + "1 : (2, 2) \n" + "2 : (1, 1) \n";
        Assert.assertEquals(output, outContent.toString());

    }

    @Test
    public void TestCase3() {
        lfu.set(1, 1);
        lfu.set(1, 1);
        lfu.set(1, 1);
        lfu.set(2, 2);
        lfu.set(2, 2);
        lfu.set(3, 3);

        lfu.printLFU();
        String output = "";
        output = output + "1 : (3, 3) \n" + "2 : (2, 2) \n" + "3 : (1, 1) \n";
        Assert.assertEquals(output, outContent.toString());

        lfu.evict();

        lfu.printLFU();
        output = output + "2 : (2, 2) \n" + "3 : (1, 1) \n";
        Assert.assertEquals(output, outContent.toString());

        lfu.set(2, 2);

        lfu.printLFU();
        output = output + "3 : (1, 1) (2, 2) \n";
        Assert.assertEquals(output, outContent.toString());

        lfu.evict();

        lfu.printLFU();
        output = output + "3 : (2, 2) \n";
        Assert.assertEquals(output, outContent.toString());

    }


}