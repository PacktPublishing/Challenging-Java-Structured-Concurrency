package challenge.concurrency;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class BackpressureTaskScope<T, R> implements StructuredTaskScope.Joiner<T, R> {

    private static final Logger logger = Logger.getLogger(BackpressureTaskScope.class.getName());

    static volatile int backpressure = 10;
    private static final AtomicInteger forked = new AtomicInteger(0);

    @Override
    public boolean onFork(StructuredTaskScope.Subtask<T> subtask) {

        if (forked.get() >= backpressure) {

            logger.info("Detected backpressure ...");
            while (forked.get() >= backpressure) {}
        }
        
        forked.getAndIncrement();
        
        return false;
    }

    @Override
    public boolean onComplete(StructuredTaskScope.Subtask<T> subtask) {

        forked.getAndDecrement();
        
        return false;
    }

    @Override
    public R result() throws Throwable {
        throw new UnsupportedOperationException("Must be implemented by subclasses.");
    }
}
