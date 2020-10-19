package enigma;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Alfred Wang
 */
class Machine {
    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        Rotor temp = null;
        for (int i = 0; i < rotors.length; i++) {
            for (Rotor r : _allRotors) {
                if (r.name() == rotors[i]) {
                    temp = r;
                    break;
                }
            }
            _slots.add(temp);
        }
    }

    /** Return my rotor slots. */
    List<Rotor> getSlots() {
        return _slots;
    }

    /** Clean my rotor slots.*/
    void clean() {
        _slots = new ArrayList<>();
    }
    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != _numRotors - 1) {
            throw error("Wrong Setting Size.");
        }

        for (int i = 1; i < _slots.size(); i++) {
            _slots.get(i).set(setting.charAt(i - 1));
        }
    }

    /** Set my rotors rings to RINGS.*/
    void setRings(String rings) {
        if (rings.length() != _numRotors - 1) {
            throw error("Wrong Setting Size.");
        }

        for (int i = 1; i < _slots.size(); i++) {
            _slots.get(i).setRing(_alphabet.toInt(rings.charAt(i - 1)));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {

        boolean hasMoved = false;
        for (int i = 1; i < _slots.size(); i++) {
            if (i == _numRotors - 1
                    && !hasMoved) {
                _slots.get(i).advance();
            } else if (i != _numRotors - 1 && _slots.get(i + 1).atNotch()
                    && _slots.get(i).rotates()
                    && !hasMoved) {
                _slots.get(i).advance();
                _slots.get(i + 1).advance();
                hasMoved = true;
            } else if (i != _numRotors - 1
                    && hasMoved
                    && _slots.get(i + 1).atNotch()) {
                _slots.get(i + 1).advance();
                hasMoved = true;
            } else {
                hasMoved = false;
            }
        }

        int temp;
        temp = _plugboard.permute(c);
        for (int i = _slots.size() - 1; i >= 0; i--) {
            Rotor r = _slots.get(i);
            temp = r.convertForward(temp);
        }

        for (int i = 1; i < _slots.size(); i++) {
            temp = _slots.get(i).convertBackward(temp);
        }

        return _plugboard.invert(temp);
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly.*/
    String convert(String msg) {
        String result = "";
        for (char i : msg.toCharArray()) {
            result += _alphabet.toChar(convert(_alphabet.toInt(i)));
        }
        return result;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of rotors.*/
    private final int _numRotors;

    /** Number of pawls.*/
    private final int _pawls;

    /** All available rotors.*/
    private final Collection<Rotor> _allRotors;

    /** A list contains all my rotors.*/
    private List<Rotor> _slots = new ArrayList<>();

    /** My plugboard.*/
    private Permutation _plugboard;

}
