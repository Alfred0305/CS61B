package enigma;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Alfred Wang
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        String[] temp = cycles.split("\\) *\\(|\\(|\\)");
        _cycles = new String[temp.length - 1];
        for (int i = 1; i < temp.length; i++) {
            _cycles[i - 1] = temp[i];
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    public void addCycle(String cycle) {
        String[] temp = _cycles;
        _cycles = new String[_cycles.length + 1];
        for (int i = 0; i < temp.length; i++) {
            _cycles[i] = temp[i];
        }
        _cycles[_cycles.length - 1] = cycle;
    }


    /** Return the value of P modulo the size of this permutation.*/
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int newP = wrap(p);
        for (int i = 0; i < _cycles.length; i++) {
            int index = _cycles[i].indexOf(_alphabet.toChar(newP));
            if (index >= 0) {
                return _alphabet.toInt(
                        _cycles[i].charAt((index + 1) % _cycles[i].length()));
            }
        }
        return newP;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int newC = wrap(c);
        for (int i = 0; i < _cycles.length; i++) {
            int index = _cycles[i].indexOf(_alphabet.toChar(newC));
            if (index >= 0) {
                return _alphabet.toInt(_cycles[i].charAt(
                        (index + _cycles[i].length() - 1)
                                % _cycles[i].length()));
            }
        }
        return newC;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int position = _alphabet.toInt(p);
        int result = permute(position);
        return _alphabet.toChar(wrap(result));

    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        int position = _alphabet.toInt(c);
        int result = invert(position);
        return _alphabet.toChar(wrap(result));
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return the cycles of this Permutation.*/
    String[] cycles() {
        return _cycles;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < _alphabet.size(); i++) {
            if (i == permute(i)) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation.*/
    private Alphabet _alphabet;
    /** A string array of all cycles.*/
    private String[] _cycles;
}
