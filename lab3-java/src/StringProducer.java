import interfaces.Producer;
import interfaces.Storage;

public class StringProducer implements Producer<String>, Runnable {
    private static int ID = 0;
    private final Storage<String> storage;
    private final int itemsToProduce;
    private final int id;

    public StringProducer(Storage<String> storage, int itemsToProduce) {
        this.storage = storage;
        this.itemsToProduce = itemsToProduce;
        id = ++ID;
    }

    @Override
    public void run() {
        for (int i = 0; i < itemsToProduce; i++) {
            String item = String.format("An item from producer [%d], #%d", id, i);
            add(item);
        }
        System.out.printf("Producer [%d] finished!%n", id);
    }

    @Override
    public void add(String item) {
        try {
            storage.add(item);
            System.out.printf("Producer [%d] put an item in the storage:\n%s\n", id, item);
        } catch (InterruptedException e) {
            System.err.println("An error occurred when trying to add an item to the storage:");
            System.err.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
