package challenge.concurrency;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceCounter implements Runnable {

    AtomicReference<Integer> counter = new AtomicReference<>(0);

    @Override
    public void run() {

        while (true) {
            Integer counterValue = counter.get();

            int newCounterValue = counterValue;

            newCounterValue++;

            if (counter.compareAndSet(counterValue, newCounterValue)) {
                return;
            }
        }
    }

    public int getCounter() {
        return counter.get();
    }
}
