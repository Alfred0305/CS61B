package enigma;
import static enigma.EnigmaException.error;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Alfred Wang
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        for (int i = 0; i < chars.length(); i++) {
            for (int j = 0; j < chars.length(); j++) {
                if (i != j && chars.charAt(i) == chars.charAt(j)) {
                    throw error("No character may be duplicated");
                }
            }
        }
        _chars = chars;
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _chars.length();
    }

    /** Returns true if preprocess(CH) is in this alphabet. */
    boolean contains(char ch) {
        return _chars.indexOf(ch) > 0;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return _chars.charAt(index);
    }

    /** Returns the index of character preprocess(CH), which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        return _chars.indexOf(ch);
    }

    /** All characters in my alphabet.*/
    private String _chars;
}
