package tablut;

import org.junit.Test;
import static org.junit.Assert.*;
import ucb.junit.textui;

/** The suite of all JUnit tests for the tablut package.
 *  @author Alfred Wang
 */
public class UnitTest {

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class, TablutTests.class);
    }

    /** A dummy test as a placeholder for real ones. */
    @Test
    public void dummyTest() {
        Board board = new Board();
        assertFalse(board.isLegal(Square.sq(7, 4), Square.sq(6, 4)));

    }
}


