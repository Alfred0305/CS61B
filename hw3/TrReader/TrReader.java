import com.sun.tools.javah.JavahTask;

import java.io.Reader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author Alfred Wang
 */
public class TrReader extends Reader {
    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     *  in STR unchanged.  FROM and TO must have the same length. */
    public TrReader(Reader str, String from, String to) {
        // TODO: YOUR CODE HERE

//        _from = new ArrayList<>();
//        for(char i : from.toCharArray()){
//            _from.add(i);
//        }
//
//        _to = new ArrayList<>();
//        for(char i : to.toCharArray()){
//            _from.add(i);
//        }
        _from = from;
        _to = to;
        _reader = str;
    }

    /* TODO: IMPLEMENT ANY MISSING ABSTRACT METHODS HERE
     * NOTE: Until you fill in the necessary methods, the compiler will
     *       reject this file, saying that you must declare TrReader
     *       abstract. Don't do that; define the right methods instead!
     */
    /**
     * Reads characters into a portion of an array.  This method will block
     * until some input is available, an I/O error occurs, or the end of the
     * stream is reached.
     *
     * @param cbuf Destination buffer
     * @param off  Offset at which to start storing characters
     * @param len  Maximum number of characters to read
     * @return The number of characters read, or -1 if the end of the
     * stream has been reached
     * @throws IOException               If an I/O error occurs
     * @throws IndexOutOfBoundsException If {@code off} is negative, or {@code len} is negative,
     *                                   or {@code len} is greater than {@code cbuf.length - off}
     */
    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int numRead = 0;
        Objects.checkFromIndexSize(off, len, cbuf.length);
        int size = _reader.read(cbuf, off, len);

        if(size != -1){
            for(int i = 0; i < len; i++) {
                if (_from.indexOf(cbuf[i]) != -1) {
                    cbuf[i] = _to.charAt(_from.indexOf(cbuf[i]));
                }
            }
        }
        return size;
//                int c = ((StringReader) _reader).read();
//                if(_from.contains((char) c)){
//                    int index = _from.indexOf((char) c);
//                    cbuf[i] = _to.get(index);
//                }else{
//                    cbuf[i] = (char) c;
//                }
//                numRead++;
//             }
    }



    /**
     * Closes the stream and releases any system resources associated with
     * it.  Once the stream has been closed, further read(), ready(),
     * mark(), reset(), or skip() invocations will throw an IOException.
     * Closing a previously closed stream has no effect.
     *
     * @throws IOException If an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        _reader.close();
    }

//    private List<Character> _from;
//    private List<Character> _to;
    private String _from;
    private String _to;
    private Reader _reader;
}
