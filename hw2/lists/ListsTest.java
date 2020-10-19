package lists;

import image.In;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 *  @author Alfred Wang
 */

public class ListsTest {
    @Test
    public void testNaturalRun(){
        IntList L = IntList.list(1, 3, 7, 5, 4, 6, 9, 10, 10, 11);
        IntList aa = IntList.list(1, 3, 7);
        IntList bb = IntList.list(5);
        IntList cc = IntList.list(4,6,9,10);
        IntList dd = IntList.list(10,11);
        IntList a = Lists.naturalRuns(L).head;
        IntList b = Lists.naturalRuns(L).tail.head;
        IntList c = Lists.naturalRuns(L).tail.tail.head;
        IntList d = Lists.naturalRuns(L).tail.tail.tail.head;

        assertEquals(aa, a);
        assertEquals(bb, b);
        assertEquals(cc, c);
        assertEquals(dd, d);
    }



    // It might initially seem daunting to try to set up
    // IntListList expected.
    //
    // There is an easy way to get the IntListList that you want in just
    // few lines of code! Make note of the IntListList.list method that
    // takes as input a 2D array.

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
