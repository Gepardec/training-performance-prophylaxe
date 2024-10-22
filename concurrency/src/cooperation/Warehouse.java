package cooperation;

public class Warehouse {
    private String goods;
    private boolean empty = true;

    public synchronized String take() {
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                // no op
            }
        }
        empty = true;
        notifyAll();
        return goods;
    }

    public synchronized void put(String goods) {
        while (!empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                // no op
            }
        }
        empty = false;
        this.goods = goods;
        notifyAll();
    }

    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse();
        Thread producer = new Thread(new Producer(warehouse));
        Thread consumer = new Thread(new Consumer(warehouse));
        producer.start();
        consumer.start();
    }
}
