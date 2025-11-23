package challenge.concurrency;

import java.util.concurrent.atomic.LongAdder;

public class AdderOperation implements Runnable {
  
    private static final LongAdder counter = new LongAdder();

    @Override
    public void run() {
        counter.add(1);
    }

    public long getCounter() {
        return counter.longValue(); // or sum()
    }
}
