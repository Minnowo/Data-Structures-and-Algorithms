
public class Main 
{
    /**
     * Returns the index of the given value or -1
     */
    public static int binarySearch(int [] arr, int value)
    {
        int lower = 0;
        int upper = arr.length - 1;

        while(lower <= upper)
        {
            int mid = (upper + lower) / 2;

            if(value < arr[mid])
            {
                upper = mid - 1;
                continue;
            }

            if(value > arr[mid])
            {
                lower = mid + 1;
                continue;
            }

            return mid;
        }

        return -1;
    }


    /**
     * Returns the index of the found value or the next closest value
     */
    public static int binarySearchClosest(int[] arr, int value)
    {
        if(value < arr[0])
        {
            return 0;
        }
        else if(value > arr[arr.length - 1])
        {
            return arr.length - 1;
        }

        int lower = 0;
        int upper = arr.length - 1;

        while(lower <= upper)
        {
            int mid = (upper + lower) / 2;

            if(value < arr[mid])
            {
                upper = mid - 1;
                continue;
            }

            if(value > arr[mid])
            {
                lower = mid + 1;
                continue;
            }

            return mid;
        }

        // lo == hi + 1
        if((arr[lower] - value) < (value - arr[upper]))
            return lower;

        return upper;
    }


    public static void main(String[] args) 
    {
        final int[] ARR = new int[] { 1, 2, 3, 4, 5, 6 };

        System.out.printf("the index of %d is %d\n", 5, binarySearch(ARR, 5));
        System.out.printf("the index of the value closest to %d is %d\n", 8, binarySearchClosest(ARR, 8));
    }
}
