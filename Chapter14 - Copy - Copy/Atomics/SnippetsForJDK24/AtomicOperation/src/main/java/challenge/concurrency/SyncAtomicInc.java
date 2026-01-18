package challenge.concurrency;

public class SyncAtomicInc {

    private int counter;
    
    public synchronized int inc() {
        return counter++;
    }

    public synchronized int getCounter() {
        return counter;
    }

}
