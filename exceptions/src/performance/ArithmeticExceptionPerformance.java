package performance;

public class ArithmeticExceptionPerformance {

    public static final int ITERATIONS = 100_000_000;

    public static void main(String[] args) {
        ArithmeticExceptionPerformance performance = new ArithmeticExceptionPerformance();

        {
            long start = System.currentTimeMillis();
            for(int i = 0; i < ITERATIONS; i++) {
                performance.divideByZeroWithChecks(4, 0);
            }
            long end = System.currentTimeMillis();
            System.out.println("divideByZeroWithCheck took " + (end - start) + "ms.");
        }

        {
            long start = System.currentTimeMillis();
            for(int i = 0; i < ITERATIONS; i++) {
                performance.divideByZeroWithException(4, 0);
            }
            long end = System.currentTimeMillis();
            System.out.println("divideByZeroWithException took " + (end - start) + "ms.");
        }
    }

    private void divideByZeroWithChecks(int x, int y) {
        if (y != 0) {
            int result = x / y;
        }
    }

    private void divideByZeroWithException(int x, int y) {
        try {
            int result = x / y;
        } catch (ArithmeticException e) {
            // no op
        }
    }
}
