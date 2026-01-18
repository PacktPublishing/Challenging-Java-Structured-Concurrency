package challenge.concurrency;

public class NonAtomicIncRunnable implements Runnable {

    private int counter;

    @Override
    public void run() {       
        counter++;
    }

    public int getCounter() {
        return counter;
    }
}
