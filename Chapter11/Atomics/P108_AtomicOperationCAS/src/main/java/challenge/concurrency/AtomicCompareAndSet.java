package challenge.concurrency;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCompareAndSet implements Runnable {

    private static final AtomicInteger counter = new AtomicInteger();

    @Override
    public void run() {

        int counterValue;
        do {
            counterValue = counter.get();
        } while (!counter.compareAndSet(counterValue, counterValue + 1));
    }

    public int getCounter() {
        return counter.get();
    }
}
