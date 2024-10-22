package performance;

public class ArrayOutOfBoundsExceptionPerformance {

    public static final int ITERATIONS = 100_000_000;


    public static void main(String[] args) {
        ArrayOutOfBoundsExceptionPerformance performance = new ArrayOutOfBoundsExceptionPerformance();

        int[] array = { 3, 6, 9, 3, 5, 8, 5, 7, 6, 7, 6, 4, 7, 4, 4, 7 };

        {
            long start = System.currentTimeMillis();
            for(int i = 0; i < ITERATIONS; i++) {
                performance.iterateWithCondition(array);
            }
            long end = System.currentTimeMillis();
            System.out.println("iterateWithCondition took " + (end - start) + "ms.");
        }

        {
            long start = System.currentTimeMillis();
            for(int i = 0; i < ITERATIONS; i++) {
                performance.iterateWithException(array);
            }
            long end = System.currentTimeMillis();
            System.out.println("iterateWithException took " + (end - start) + "ms.");
        }
    }

    private int iterateWithCondition(int[] array) {
        int sum = 0;
        for(int i = 0; i < array.length; i++) {
            sum += array[i];
        }
        return sum;
    }

    private int iterateWithException(int[] array) {
        int sum = 0;
        int i = 0;
        while(true) {
            try {
                sum += array[i++];
            } catch(ArrayIndexOutOfBoundsException e) {
                break;
            }
        }
        return sum;
    }
}
