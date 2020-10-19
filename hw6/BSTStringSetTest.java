import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author Alfred Wang
 */
public class BSTStringSetTest  {

    @Test
    public void testPut() {
        BSTStringSet s = new BSTStringSet();
        s.put("D");
        s.put("A");
        s.put("B");
        s.put("E");
        Iterator<String> temp = s.iterator();
        assertEquals("A", temp.next());
        assertEquals("B", temp.next());
        assertEquals("D", temp.next());
        assertEquals("E", temp.next());
    }
}
