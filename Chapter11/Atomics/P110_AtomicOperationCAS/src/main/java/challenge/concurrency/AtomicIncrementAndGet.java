package challenge.concurrency;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIncrementAndGet implements Runnable {

    private static final AtomicInteger counter = new AtomicInteger();

    @Override
    public void run() {
        counter.incrementAndGet();
    }

    public int getCounter() {
        return counter.get();
    }
}
