package dead;

public class Deadlock {

    public static void main(String[] args) {
        final Object resourceA = new Object();
        final Object resourceB = new Object();

        Thread t1 = new Thread(() -> {
            synchronized (resourceA) {
                System.out.println("Thread 1: locked resource A");

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    // no op
                }

                synchronized (resourceB) {
                    System.out.println("Thread 1: locked resource B");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (resourceB) {
                System.out.println("Thread 2: locked resource B");

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    // no op
                }

                synchronized (resourceA) {
                    System.out.println("Thread 2: locked resource A");
                }
            }
        });

        t1.start();
        t2.start();
    }
}
