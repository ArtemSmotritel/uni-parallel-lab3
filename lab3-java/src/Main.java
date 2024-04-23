import java.util.Arrays;

public class Main {
    private static final int STORAGE_CAPACITY = 2;
    private static final int[] itemsToProduceForEachProducer = new int[]{ 2, 2 };
    private static final int[] itemsToConsumeForEachConsumer = new int[]{ 5 };

    public static void main(String[] args) {
        var storage = new SimpleStorage(STORAGE_CAPACITY);

        for (int k : itemsToProduceForEachProducer) {
            var producer = new SimpleProducer(storage, k);
            new Thread(producer).start();
        }

        for (int j : itemsToConsumeForEachConsumer) {
            var consumer = new SimpleConsumer(storage, j);
            new Thread(consumer).start();
        }

        int producedItemsTotal = Arrays.stream(itemsToProduceForEachProducer).sum();
        int consumedItemsTotal = Arrays.stream(itemsToConsumeForEachConsumer).sum();

        if (producedItemsTotal == consumedItemsTotal) {
            return;
        }

        if (producedItemsTotal > consumedItemsTotal) {
            var dummyConsumer = new DummyConsumer(storage, producedItemsTotal - consumedItemsTotal);
            new Thread(dummyConsumer).start();
        } else {
            var dummyProducer = new DummyProducer(storage, consumedItemsTotal - producedItemsTotal);
            new Thread(dummyProducer).start();
        }
    }
}