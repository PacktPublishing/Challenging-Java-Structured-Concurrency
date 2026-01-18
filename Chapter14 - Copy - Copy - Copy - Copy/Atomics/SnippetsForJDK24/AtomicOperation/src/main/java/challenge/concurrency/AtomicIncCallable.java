package challenge.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIncCallable implements Callable {

    private static final AtomicInteger counter = new AtomicInteger();

    @Override
    public Integer call() throws Exception {
        return counter.incrementAndGet();
    }

    public int getCounter() {
        return counter.get();
    }
}
