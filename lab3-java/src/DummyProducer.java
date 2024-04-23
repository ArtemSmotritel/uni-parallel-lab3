import interfaces.Storage;

public class DummyProducer extends SimpleProducer {
    public DummyProducer(Storage storage, int itemsToProduce) {
        super(storage, itemsToProduce);
    }

    @Override
    public void run() {
        for (int i = 0; i < itemsToProduce; i++) {
            String item = String.format("A DUMMY item from a DUMMY producer [%d], #%d", id, i);
            add(item);
        }
        System.err.printf("DUMMY producer [%d] finished!%n", id);
    }

    @Override
    public void add(String item) {
        try {
            storage.add(item);
            System.err.printf("Producer [%d] put an item in the storage:\n%s\n", id, item);
        } catch (InterruptedException e) {
            System.err.println("An error occurred when trying to add an item to the storage:");
            System.err.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
