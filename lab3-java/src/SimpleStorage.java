import interfaces.Storage;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class SimpleStorage implements Storage {
    private final LinkedList<String> tList;
    private final Semaphore accessSemaphore;
    private final Semaphore fullSemaphore;
    private final Semaphore emptySemaphore;

    public SimpleStorage(int capacity) {
        this.tList = new LinkedList<>();
        accessSemaphore = new Semaphore(1);
        fullSemaphore = new Semaphore(capacity);
        emptySemaphore = new Semaphore(0);
    }

    @Override
    public String get() throws InterruptedException {
        emptySemaphore.acquire();
        accessSemaphore.acquire();

        String item = tList.removeFirst();

        accessSemaphore.release();
        fullSemaphore.release();
        return item;
    }

    @Override
    public void add(String item) throws InterruptedException {
        fullSemaphore.acquire();
        accessSemaphore.acquire();

        tList.addLast(item);

        accessSemaphore.release();
        emptySemaphore.release();
    }
}
