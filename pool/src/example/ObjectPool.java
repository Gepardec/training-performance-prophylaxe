package example;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ObjectPool {

    private static final int POOL_SIZE = 2;

    private final BlockingQueue<ExpensiveObject> pool;

    public ObjectPool() {
        pool = new ArrayBlockingQueue<ExpensiveObject>(POOL_SIZE);
        for (int i = 0; i < POOL_SIZE; i++) {
            pool.add(new ExpensiveObject(i));
        }
    }

    public ExpensiveObject borrowObject() throws InterruptedException {
        return pool.take();
    }

    public void returnObject(ExpensiveObject object) {
        pool.offer(object);
    }

    public static void main(String[] args) throws InterruptedException {
        ObjectPool pool = new ObjectPool();

        ExpensiveObject object = pool.borrowObject();
        object.use();
        pool.returnObject(object);

        ExpensiveObject object2 = pool.borrowObject();
        object2.use();
        pool.returnObject(object2);

        ExpensiveObject object3 = pool.borrowObject();
        object3.use();
        pool.returnObject(object3);
    }

    public static class ExpensiveObject {
        // something complex is happening here...
        private final int id;

        public ExpensiveObject(int id) {
            this.id = id;
        }

        public void use() {
            System.out.println("Using object " + id);
        }
    }
}
