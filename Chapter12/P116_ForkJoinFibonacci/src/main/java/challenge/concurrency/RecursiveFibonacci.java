package challenge.concurrency;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.logging.Logger;

public class RecursiveFibonacci extends RecursiveAction {

    private static final Logger logger = Logger.getLogger(RecursiveFibonacci.class.getName());

    private static final long THRESHOLD = 5;
    private volatile long nr;

    public RecursiveFibonacci(long nr) {
        this.nr = nr;
    }

    @Override
    protected void compute() {

        final long n = nr;
        if (n <= THRESHOLD) {
            nr = fibonacci(n);
        } else {
                               
            RecursiveFibonacci nMinusOne = new RecursiveFibonacci(n - 1);
            RecursiveFibonacci nMinusTwo = new RecursiveFibonacci(n - 2);
            ForkJoinTask.invokeAll(nMinusOne, nMinusTwo);
            
            nr = nMinusOne.fNumber() + nMinusTwo.fNumber();             
            
            /*
            nr = ForkJoinTask.invokeAll(createSubtasks(n))
                    .stream()
                    .mapToLong(x -> x.fNumber())
                    .sum();            
            */
        }
    }
    
    /*
    private List<RecursiveFibonacci> createSubtasks(long n) {

        List<RecursiveFibonacci> subtasks = new ArrayList<>();

        RecursiveFibonacci nMinusOne = new RecursiveFibonacci(n - 1);
        RecursiveFibonacci nMinusTwo = new RecursiveFibonacci(n - 2);

        subtasks.add(nMinusOne);
        subtasks.add(nMinusTwo);

        return subtasks;
    }  
    */

    private long fibonacci(long n) {

        logger.info(() -> "Number: " + n
                + " Thread: " + Thread.currentThread().getName());

        if (n <= 1) {
            return n;
        }

        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    public long fNumber() {
        return nr;
    }
}
