import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuickSelect {

    public static void main(String[] args) {
        // Input sizes for the experiment
        int[] inputLengths = {20, 50, 100, 200, 300};

        long[] experimentalValues = new long[inputLengths.length];

        // JVM Warm-up: Running the algorithm a few times before timing actual inputs
        int[] JVMWarmupArray = generateRandomArray(50);
        for (int i = 0; i < 5; i++) {
            quickSelect(JVMWarmupArray, 0, JVMWarmupArray.length - 1, JVMWarmupArray.length / 2);
        }

       
        for (int i = 0; i < inputLengths.length; i++) {
            int n = inputLengths[i];
            int[] a = generateRandomArray(n);

            long startTime = System.nanoTime();
            quickSelect(a, 0, a.length - 1, n / 2);  // Perform QuickSelect to find the median element
            long endTime = System.nanoTime();

            experimentalValues[i] = endTime - startTime;
        }

        // Output the data
        System.out.println("Input n | Practical Readings (ns)");
        for (int i = 0; i < inputLengths.length; i++) {
            System.out.println(inputLengths[i] + " | " + experimentalValues[i]);
        }
    }

    // Function to generate a random array of size n
    public static int[] generateRandomArray(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = (int) (Math.random() * 1000);
        }
        return a;
    }

    // QuickSelect algorithm (returns the k-th smallest element in the array)
    public static int quickSelect(int[] a, int left, int right, int k) {
        if (right - left + 1 < 10) {
            Arrays.sort(a, left, right + 1);  // Sort small subarrays
            return a[left + k];
        }

        // Divide the array into groups of 5 and find medians
        List<Integer> medians = new ArrayList<>();
        for (int i = left; i <= right; i += 5) {
            int subRight = Math.min(i + 4, right);  // Ensure the group doesn't go beyond array bounds
            medians.add(getMedian(a, i, subRight));
        }

        // Get the median of medians
        int[] mediansArray = medians.stream().mapToInt(Integer::intValue).toArray();
        int medianOfMedians = quickSelect(mediansArray, 0, mediansArray.length - 1, mediansArray.length / 2);

        // Partition the array based on the median of medians
        int pivotIndex = partition(a, left, right, medianOfMedians);
        int numOnLeft = pivotIndex - left + 1;  // Number of elements in the left partition

        if (k < numOnLeft) {
            return quickSelect(a, left, pivotIndex - 1, k);
        } else if (k > numOnLeft) {
            return quickSelect(a, pivotIndex + 1, right, k - numOnLeft);
        } else {
            return a[pivotIndex];
        }
    }
    
 // function to swap elements
    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Function to get the median of a sublist (group of up to 5 elements)
    public static int getMedian(int[] a, int start, int end) {
        int[] subArray = Arrays.copyOfRange(a, start, end + 1);
        Arrays.sort(subArray);
        return subArray[subArray.length / 2];
    }

   
    public static int partition(int[] a, int left, int right, int pivotValue) {
        for (int i = left; i <= right; i++) {
            if (a[i] == pivotValue) {
                swap(a, i, right);
                break;
            }
        }

        int pivot = a[right];
        int i = left;

        // Move elements smaller than the pivot to the left
        for (int j = left; j < right; j++) {
            if (a[j] <= pivot) {
                swap(a, i, j);
                i++;
            }
        }

        // Move pivot to its correct place
        swap(a, i, right);
        return i;
    }

    
}
