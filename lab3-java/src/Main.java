public class Main {
    private static final int STORAGE_CAPACITY = 2;
    private static final int[] itemsToProduceForEachProducer = new int[]{1, 3, 4};
    private static final int[] itemsToConsumeForEachConsumer = new int[]{4, 3, 1};

    public static void main(String[] args) {
        var storage = new StringStorage(STORAGE_CAPACITY);

        for (int k : itemsToProduceForEachProducer) {
            var producer = new StringProducer(storage, k);
            new Thread(producer).start();
        }

        for (int j : itemsToConsumeForEachConsumer) {
            var consumer = new StringConsumer(storage, j);
            new Thread(consumer).start();
        }

    }
}