import interfaces.Storage;

import java.util.function.Consumer;

public class StringConsumer implements Consumer<String>, Runnable {
    private static int ID = 0;
    private final Storage<String> stringStorage;
    private final int id;
    private final int itemsToConsume;

    public StringConsumer(Storage<String> stringStorage, int itemsToConsume) {
        this.stringStorage = stringStorage;
        this.id = ++ID;
        this.itemsToConsume = itemsToConsume;
    }

    @Override
    public void accept(String s) {
        System.out.printf("Consumer [%d] accepted an item:\n%s\n", id, s);
    }

    @Override
    public Consumer<String> andThen(Consumer<? super String> after) {
        return Consumer.super.andThen(after);
    }

    @Override
    public void run() {
        for (int i = 0; i < itemsToConsume; i++) {
            String item = null;
            try {
                item = stringStorage.get();
                accept(item);
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.err.println("An error occurred when trying to consume an item from the storage:");
                System.err.println(e.getMessage());
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        System.out.printf("Consumer [%d] finished!%n", id);
    }
}
