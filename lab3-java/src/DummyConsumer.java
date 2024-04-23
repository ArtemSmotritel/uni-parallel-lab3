import interfaces.Storage;

public class DummyConsumer extends SimpleConsumer {
    public DummyConsumer(Storage stringStorage, int itemsToConsume) {
        super(stringStorage, itemsToConsume);
    }

    @Override
    public void accept(String s) {
        System.err.printf("Dummy consumer [%d] accepted an item:\n%s\n", id, s);
    }
}
