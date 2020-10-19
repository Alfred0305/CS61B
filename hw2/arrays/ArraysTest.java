package arrays;

import afu.org.checkerframework.common.value.qual.StaticallyExecutable;
import com.sun.xml.internal.xsom.impl.scd.Iterators;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *  @author Alfred Wang
 */

public class ArraysTest {
    @Test
    public void testCatenate(){
        int[] a = {1,2,3};
        int[] b = {4,5,6};
        int[] c = {1,2,3,4,5,6};

        assertArrayEquals(c, Arrays.catenate(a,b));
    }

    @Test
    public void testRemove(){
        int[] a = {1,2,3,4,5,6};
        int[] b = {4,5,6};
        assertArrayEquals(b, Arrays.remove(a,0,3));
    }

    @Test
    public void testNaturalRuns(){
        int[] a = {1, 3, 7, 5, 4, 6, 9, 10};
        int[][] b = {{1, 3, 7}, {5}, {4, 6, 9, 10}};
        assertArrayEquals(b,Arrays.naturalRuns(a));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
