package challenge.concurrency;

import java.util.concurrent.atomic.LongAccumulator;

public class AccumulatorOperation implements Runnable {
  
     public static LongAccumulator counter 
             = new LongAccumulator(Long::sum, 0);

    @Override
    public void run() {
        counter.accumulate(1);
    }

    public long getCounter() {
        return counter.longValue(); // or get()
    }
}
