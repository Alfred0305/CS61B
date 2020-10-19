package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author Alfred Wang
 */
class Arrays {

    /* C. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        int[] result = new int[A.length+B.length];
        for(int i = 0; i < A.length;i++){
            result[i] = A[i];
        }
        for(int i = 0; i < B.length;i++){
            result[i+A.length] = B[i];
        }
        return result;
    }

    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. */
    static int[] remove(int[] A, int start, int len) {
        int[] result = new int[A.length-len];
        for(int i = 0; i < start; i++){
            result[i] = A[i];
        }
        for(int i = start+len; i < A.length; i++){
            result[i-len] = A[i];
        }
        return result;
    }

    /* E. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {
        int[][] result = new int[A.length][];
        int firstIndex = 0; int eleIndex = 0;
        int[] ele = new int[A.length];
        for(int i = 0; i < A.length-1; i++){
           if(A[i] >= A[i+1]){
               ele[eleIndex] = A[i];
               ele = remove(ele,eleIndex+1,ele.length-eleIndex-1);
               result[firstIndex] = ele;
               firstIndex ++;
               eleIndex = 0; ele = new int[A.length];
           }else{
                ele[eleIndex] = A[i];
                eleIndex ++;
           }
        }
        ele[eleIndex] = A[A.length-1];
        ele = remove(ele,eleIndex+1,ele.length-eleIndex-1);
        result[firstIndex] = ele;

        int[][] resizeResult = new int[firstIndex+1][];

        for(int i = 0; i < firstIndex+1;i++){
            resizeResult[i] = result[i];
        }
        return resizeResult;

    }

}
