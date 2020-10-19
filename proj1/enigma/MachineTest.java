package enigma;


import org.junit.Test;
import java.util.ArrayList;
import static enigma.TestUtils.NAVALA;
import static org.junit.Assert.assertEquals;

public class MachineTest {
    private Alphabet alpha = new Alphabet();
    private ArrayList<Rotor> allrotors = new ArrayList<>();

/**
 *  new MovingRotor("I", new Permutation(NAVALA.get("I"), alpha), "Q");
    new MovingRotor("II", new Permutation(NAVALA.get("II"), alpha), "E");
    new MovingRotor("III", new Permutation(NAVALA.get("III"), alpha), "V");
    new MovingRotor("IV", new Permutation(NAVALA.get("IV"), alpha), "J");
    new MovingRotor("V", new Permutation(NAVALA.get("V"), alpha), "Z");
    new MovingRotor("VI", new Permutation(NAVALA.get("VI"), alpha), "ZM");
    new MovingRotor("VII", new Permutation(NAVALA.get("VII"), alpha), "ZM");
    new MovingRotor("VIII", new Permutation(NAVALA.get("VIII"), alpha), "ZM");
    new FixedRotor("Beta", new Permutation(NAVALA.get("Beta"), alpha));
    new FixedRotor("Gamma", new Permutation(NAVALA.get("Gamma"), alpha));
    new Reflector("B", new Permutation(NAVALA.get("B"), alpha));
    new Reflector("C", new Permutation(NAVALA.get("C"), alpha));
 */

    @Test
    public void testInsertRotors() {
        Rotor i = new MovingRotor("I",
                new Permutation(NAVALA.get("I"), alpha), "Q");
        Rotor ii = new MovingRotor("II",
                new Permutation(NAVALA.get("II"), alpha), "E");
        Rotor iii = new MovingRotor("III",
                new Permutation(NAVALA.get("III"), alpha), "V");
        Rotor b = new Reflector("B",
                new Permutation(NAVALA.get("B"), alpha));
        allrotors.add(i);
        allrotors.add(ii);
        allrotors.add(iii);
        allrotors.add(b);
        Machine machine = new Machine(alpha, 3, 2, allrotors);
        machine.insertRotors(new String[] {"B", "I", "II"});
        assertEquals(b, machine.getSlots().get(0));
        assertEquals(i, machine.getSlots().get(1));
        assertEquals(ii, machine.getSlots().get(2));
    }

    @Test
    public void testSetRotors() {
        Rotor i = new MovingRotor("I",
                new Permutation(NAVALA.get("I"), alpha), "Q");
        Rotor ii = new MovingRotor("II",
                new Permutation(NAVALA.get("II"), alpha), "E");
        Rotor iii = new MovingRotor("III",
                new Permutation(NAVALA.get("III"), alpha), "V");
        Rotor b = new Reflector("B",
                new Permutation(NAVALA.get("B"), alpha));
        allrotors.add(i);
        allrotors.add(ii);
        allrotors.add(iii);
        allrotors.add(b);
        Machine machine = new Machine(alpha, 3, 2, allrotors);
        machine.insertRotors(new String[] {"B", "I", "II"});
        machine.setRotors("BC");
        assertEquals(0, machine.getSlots().get(0).setting());
        assertEquals(1, machine.getSlots().get(1).setting());
        assertEquals(2, machine.getSlots().get(2).setting());
        machine.setRotors("AA");
        assertEquals(0, machine.getSlots().get(1).setting());
        assertEquals(0, machine.getSlots().get(2).setting());

    }


    @Test
    public void testConvert() {
        Rotor i = new MovingRotor("I",
                new Permutation(NAVALA.get("I"), alpha), "Q");
        Rotor ii = new MovingRotor("II",
                new Permutation(NAVALA.get("II"), alpha), "E");
        Rotor iii = new MovingRotor("III",
                new Permutation(NAVALA.get("III"), alpha), "V");
        Rotor b = new Reflector("B",
                new Permutation(NAVALA.get("B"), alpha));
        allrotors.add(i);
        allrotors.add(ii);
        allrotors.add(iii);
        allrotors.add(b);
        Machine machine = new Machine(alpha, 3, 2, allrotors);
        machine.insertRotors(new String[] {"B", "II", "I"});
        machine.setPlugboard(new Permutation("", alpha));
        assertEquals("QV", machine.convert("AB"));
    }

    @Test
    public void testConvert2() {
        Rotor i = new MovingRotor("I",
                new Permutation(NAVALA.get("I"), alpha), "Q");
        Rotor ii = new MovingRotor("II",
                new Permutation(NAVALA.get("II"), alpha), "E");
        Rotor iii = new MovingRotor("III",
                new Permutation(NAVALA.get("III"), alpha), "V");
        Rotor beta = new FixedRotor("Beta",
                new Permutation(NAVALA.get("Beta"), alpha));
        Rotor b = new Reflector("B",
                new Permutation(NAVALA.get("B"), alpha));
        allrotors.add(i);
        allrotors.add(ii);
        allrotors.add(iii);
        allrotors.add(beta);
        allrotors.add(b);
        Machine machine = new Machine(alpha, 5, 3, allrotors);
        machine.insertRotors(new String[] {"B", "Beta", "I", "II", "III"});
        machine.setPlugboard(new Permutation("", alpha));
        assertEquals("ILBDAAMTAZ", machine.convert("HELLOWORLD"));
    }

}
