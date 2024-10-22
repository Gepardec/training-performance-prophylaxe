package cooperation;

public class Consumer implements Runnable {
    private final Warehouse warehouse;

    public Consumer(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        for (String goods = warehouse.take(); goods != null; goods = warehouse.take()) {
            System.out.println("Consumed " + goods);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // no op
            }
        }
    }
}
