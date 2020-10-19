package image;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author FIXME
 */

public class MatrixUtilsTest {

    @Test
    public void testAccumulateVertical(){
        double[][] m = new double[][]{{1000000,1000000,1000000,1000000},
                {1000000,75990,30003,1000000},
                {1000000,30002,103046,1000000},
                {1000000,29515,38273,1000000},
                {1000000,73403,35399,1000000},
                {1000000,1000000,1000000,1000000}};

        double[][] a = new double[][]{{1000000,1000000,1000000,1000000},
                {2000000,1075990,1030003,2000000},
                {2075990,1060005,1133049,2030003},
                {2060005,1089520,1098278,2133049},
                {2089520,1162923,1124919,2098278},
                {2162923,2124919,2124919,2124919}};

        assertArrayEquals(a,MatrixUtils.accumulateVertical(m));
    }

    @Test
    public void testAccumulate(){
        double[][] m = new double[][]{{1000000,1000000,1000000,1000000,1000000,1000000},
                {1000000,75990,30002,29515,73403,1000000},
                {1000000,30003,103046,38273,35399,1000000},
                {1000000,1000000,1000000,1000000,1000000,1000000}};

        double[][] a = new double[][]{{1000000,2000000,2075990,2060005,2089520,2162923},
                {1000000,1075990,1060005,1089520,1162923,2124919},
                {1000000,1030003,1133049,1098278,1124919,2124919},
                {1000000,2000000,2030003,2133049,2098278,2124919}};

        assertArrayEquals(a,MatrixUtils.accumulate(m, MatrixUtils.Orientation.HORIZONTAL));
    }

    @Test
    public void testHelper(){
        double[][] a = new double[][]{{1,2,3},
                {4,5,6}};

        double[][] b = new double[][]{{1,4},
                {2,5},
                {3,6}};

        assertArrayEquals(b,MatrixUtils.helper(a));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(MatrixUtilsTest.class));
    }
}
