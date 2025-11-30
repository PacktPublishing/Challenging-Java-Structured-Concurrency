package challenge.concurrency;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.atomic.LongAdder;
import java.util.logging.Logger;

record RecursiveScopedFibonacci(long nr) {

    private static final Logger logger = Logger.getLogger(RecursiveScopedFibonacci.class.getName());
    private static final LongAdder fadder = new LongAdder();

    private static final int THRESHOLD = 5;

    public static long execute(long nr) throws InterruptedException {

        try (var scope = StructuredTaskScope.open()) {

            logger.info("Fibonacci computation start!");

            new RecursiveScopedFibonacci(nr).compute(nr, scope);

            scope.join();

            logger.info(() -> "Fibonacci computation end!");

            return fadder.sum();
        }
    }

    protected long compute(long nr, StructuredTaskScope<Object, Void> scope) {

        final long n = nr;
        if (n <= THRESHOLD) {

            nr = f(n);
            fadder.add(nr);
        } else {
            RecursiveScopedFibonacci taskLeft = new RecursiveScopedFibonacci(n - 1);
            RecursiveScopedFibonacci taskRight = new RecursiveScopedFibonacci(n - 2);
            
            scope.fork(() -> taskLeft.compute(n - 1, StructuredTaskScope.open()));
            scope.fork(() -> taskRight.compute(n - 2, StructuredTaskScope.open()));
        }
        
        return nr;
    }

    private long f(long n) {        

        // simulate a potential I/O-bound task part
        try { Thread.sleep((long) (Math.random() * 1000)); } catch (InterruptedException ex) {}

        logger.info(() -> "Thread: #" + Thread.currentThread().threadId() 
                + " Fibonacci: " + n);
        
        if (n <= 1) {
            return n;
        }

        return f(n - 1) + f(n - 2);
    }    
}
