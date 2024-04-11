package interfaces;

public interface Storage<T>  {
    T get() throws InterruptedException;
    void add(T item) throws InterruptedException;
}
