import java.util.Iterator;
import utils.Filter;

/** A kind of Filter that lets all the VALUE elements of its input sequence
 *  that are larger than all the preceding values to go through the
 *  Filter.  So, if its input delivers (1, 2, 3, 3, 2, 1, 5), then it
 *  will produce (1, 2, 3, 5).
 *  @author You
 */
class MonotonicFilter<Value extends Comparable<Value>> extends Filter<Value> {

    /** A filter of values from INPUT that delivers a monotonic
     *  subsequence.  */
    MonotonicFilter(Iterator<Value> input) {
        super(input);
//        currentLargest = next();
        currentLargest = null;
    }

    @Override
    protected boolean keep() {
        if (currentLargest == null){
            currentLargest = candidateNext();
            return true;
        }
        if(currentLargest.compareTo(candidateNext()) < 0){
            currentLargest = candidateNext();
//              next();
            return true;
        }


        return false;
    }
    

    private Value currentLargest;

}
