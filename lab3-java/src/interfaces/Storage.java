package interfaces;

public interface Storage  {
    String get() throws InterruptedException;
    void add(String item) throws InterruptedException;
}
