package cooperation;

public class Producer implements Runnable {
    private final Warehouse warehouse;

    public Producer(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        String[] batch = new String[]{"Apples", "Oranges", "Coconuts", "Cucumbers"};
        for (String goods : batch) {
            warehouse.put(goods);
            System.out.println("Produced " + goods);
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                // no op
            }
        }
        warehouse.put(null);
    }
}
