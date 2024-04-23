import java.util.Arrays;
import java.util.concurrent.Executors;

public class Main {
    private static final int STORAGE_CAPACITY = 2;
    private static final int[] itemsToProduceForEachProducer = new int[]{ 2, 2 };
    private static final int[] itemsToConsumeForEachConsumer = new int[]{ 5 };

    public static void main(String[] args) {
        var storage = new SimpleStorage(STORAGE_CAPACITY);

        var executor = Executors.newFixedThreadPool(5);

        for (int k : itemsToProduceForEachProducer) {
            var producer = new SimpleProducer(storage, k);
            executor.submit(producer);
        }

        for (int j : itemsToConsumeForEachConsumer) {
            var consumer = new SimpleConsumer(storage, j);
            executor.submit(consumer);
        }

        int producedItemsTotal = Arrays.stream(itemsToProduceForEachProducer).sum();
        int consumedItemsTotal = Arrays.stream(itemsToConsumeForEachConsumer).sum();

        if (producedItemsTotal == consumedItemsTotal) {
            return;
        }

        if (producedItemsTotal > consumedItemsTotal) {
            var dummyConsumer = new DummyConsumer(storage, producedItemsTotal - consumedItemsTotal);
            executor.submit(dummyConsumer);
        } else {
            var dummyProducer = new DummyProducer(storage, consumedItemsTotal - producedItemsTotal);
            executor.submit(dummyProducer);
        }
    }
}