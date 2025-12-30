package challenge.concurrency;

public class SyncAtomicInc {

    private int counter;
    
    public synchronized int inc() {
        return counter++;
    }

    public int getCounter() {
        return counter;
    }

}
