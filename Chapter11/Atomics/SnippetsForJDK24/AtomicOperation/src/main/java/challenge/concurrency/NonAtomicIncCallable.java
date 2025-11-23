package challenge.concurrency;

import java.util.concurrent.Callable;

public class NonAtomicIncCallable implements Callable {

    private int counter;
    
    @Override
    public Integer call() throws Exception {
        return counter++;
    }    
    
    public int getCounter() {
        return counter;
    }
}
