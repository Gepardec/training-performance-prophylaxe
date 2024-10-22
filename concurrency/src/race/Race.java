package race;

public class Race {


    public static void main(String[] args) {
        Counter counter = new Counter();

        new Thread(new Runner(counter)).start();
        new Thread(new Runner(counter)).start();
    }

    static class Counter {
        private int count;

        public void increment() {
            count++;
        }

        public int getCount() {
            return count;
        }
    }

    static class Runner implements Runnable {
        private final Counter counter;

        public Runner(Counter counter) {
            this.counter = counter;
        }

        @Override
        public void run() {
            while (true) {
                counter.increment();
                System.out.println(counter.getCount());
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    // no op
                }
            }
        }
    }
}
