import java.util.Arrays;

/**
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Distribution Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 1; i < k; ++i) {
                int tmp = array[i];
                int j = i - 1;

                while (j >= 0 && array[j] > tmp) {
                    array[j + 1] = array[j];
                    j = j - 1;
                }
                array[j + 1] = tmp;
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 0; i < k-1; i++) {
                int tmp = i;
                for (int j = i+1; j < k; j++)
                    if (array[j] < array[tmp])
                        tmp = j;
                int temp = array[tmp];
                array[tmp] = array[i];
                array[i] = temp;
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            sort(array, 0, k-1);
        }

        public void sort(int[] arr, int l, int r) {
            if (l < r)
            {
                // Find the middle point
                int m = (l+r)/2;

                // Sort first and second halves
                sort(arr, l, m);
                sort(arr , m+1, r);

                // Merge the sorted halves
                merge(arr, l, m, r);
            }
        }

        void merge(int arr[], int l, int m, int r) {

            int n1 = m - l + 1;
            int n2 = r - m;

            int L[] = new int [n1];
            int R[] = new int [n2];

            for (int i=0; i < n1; ++i)
                L[i] = arr[l + i];
            for (int j=0; j < n2; ++j)
                R[j] = arr[m + 1 + j];

            int i = 0, j = 0;
            int k = l;
            while (i < n1 && j < n2) {
                if (L[i] <= R[j]) {
                    arr[k] = L[i];
                    i++;
                }
                else {
                    arr[k] = R[j];
                    j++;
                }
                k++;
            }
            while (i < n1) {
                arr[k] = L[i];
                i++;
                k++;
            }
            while (j < n2) {
                arr[k] = R[j];
                j++;
                k++;
            }
        }
        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Distribution Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class DistributionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = k / 2 - 1; i >= 0; i--)
                heapify(array, k, i);

            for (int i=k-1; i>=0; i--) {

                int temp = array[0];
                array[0] = array[i];
                array[i] = temp;

                heapify(array, i, 0);
            }
        }

        void heapify(int arr[], int n, int i) {
            int largest = i;
            int l = 2*i + 1;
            int r = 2*i + 2;
            if (l < n && arr[l] > arr[largest])
                largest = l;

            if (r < n && arr[r] > arr[largest])
                largest = r;

            if (largest != i) {
                int swap = arr[i];
                arr[i] = arr[largest];
                arr[largest] = swap;
                heapify(arr, n, largest);
            }
        }

        @Override
        public String toString() {
            return "Distribution Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = k / 2 - 1; i >= 0; i--)
                heapify(array, k, i);

            for (int i=k-1; i>=0; i--) {

                int temp = array[0];
                array[0] = array[i];
                array[i] = temp;

                heapify(array, i, 0);
            }
        }

        void heapify(int arr[], int n, int i) {
            int largest = i;
            int l = 2*i + 1;
            int r = 2*i + 2;
            if (l < n && arr[l] > arr[largest])
                largest = l;

            if (r < n && arr[r] > arr[largest])
                largest = r;

            if (largest != i) {
                int swap = arr[i];
                arr[i] = arr[largest];
                arr[largest] = swap;
                heapify(arr, n, largest);
            }
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            sort(array, 0, k);
        }

        void sort(int[] arr, int low, int high) {
            if (low < high) {
                int pi = partition(arr, low, high);
                sort(arr, low, pi-1);
                sort(arr, pi+1, high);
            }
        }

        int partition(int arr[], int low, int high) {
            int pivot = arr[high];
            int i = (low-1); // index of smaller element
            for (int j=low; j<high; j++) {
                if (arr[j] < pivot) {
                    i++;
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
            int temp = arr[i+1];
            arr[i+1] = arr[high];
            arr[high] = temp;

            return i+1;
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            int m = getMax(a, k);
            for (int exp = 1; m/exp > 0; exp *= 10)
                countSort(a, k, exp);
        }

        static int getMax(int arr[], int n) {
            int mx = arr[0];
            for (int i = 1; i < n; i++)
                if (arr[i] > mx)
                    mx = arr[i];
            return mx;
        }

        static void countSort(int arr[], int n, int exp) {
            int output[] = new int[n];
            int i;
            int count[] = new int[10];
            Arrays.fill(count,0);

            for (i = 0; i < n; i++)
                count[ (arr[i]/exp)%10 ]++;
            for (i = 1; i < 10; i++)
                count[i] += count[i - 1];

            for (i = n - 1; i >= 0; i--) {
                output[count[ (arr[i]/exp)%10 ] - 1] = arr[i];
                count[ (arr[i]/exp)%10 ]--;
            }

            for (i = 0; i < n; i++)
                arr[i] = output[i];
        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            int m = getMax(a, k);
            int i = 1;
            while (m/i >= 10) {
                i *= 10;
            }
            for (int exp = i; exp > 0; exp /= 10)
                countSort(a, k, exp);
        }

        static int getMax(int arr[], int n) {
            int mx = arr[0];
            for (int i = 1; i < n; i++)
                if (arr[i] > mx)
                    mx = arr[i];
            return mx;
        }

        static void countSort(int arr[], int n, int exp) {
            int output[] = new int[n];
            int i;
            int count[] = new int[10];
            Arrays.fill(count,0);

            for (i = 0; i < n; i++)
                count[ (arr[i]/exp)%10 ]++;
            for (i = 1; i < 10; i++)
                count[i] += count[i - 1];

            for (i = n - 1; i >= 0; i--) {
                output[count[ (arr[i]/exp)%10 ] - 1] = arr[i];
                count[ (arr[i]/exp)%10 ]--;
            }

            for (i = 0; i < n; i++)
                arr[i] = output[i];
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
