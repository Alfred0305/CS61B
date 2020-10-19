import java.util.Iterator;
import utils.Predicate;
import utils.Filter;

/** A kind of Filter that tests the elements of its input sequence of
 *  VALUES by applying a Predicate object to them.
 *  @author You
 */
class PredicateFilter<Value> extends Filter<Value> {

    /** A filter of values from INPUT that tests them with PRED,
     *  delivering only those for which PRED is true. */
    PredicateFilter(Predicate<Value> pred, Iterator<Value> input) {
        super(input);
        myPred = pred;
    }

    @Override
    protected boolean keep() {
        if (myPred.test(candidateNext())) {
            return true;
        }
        return false;
    }

    private Predicate<Value> myPred;
}
