package challenge.concurrency;

import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.atomic.LongAdder;
import java.util.logging.Logger;

record RecursiveScopedTask(List<Integer> intlist, int start, int end) {

    private static final Logger logger = Logger.getLogger(RecursiveScopedTask.class.getName());

    private static final int THRESHOLD = 10;
    private static final LongAdder totalSum = new LongAdder();

    public static long execute(List<Integer> intlist, int start, int end) throws InterruptedException {

        try (var scope = StructuredTaskScope.open()) {

            logger.info("Computation start!");
            
            new RecursiveScopedTask(intlist, start, end).compute(intlist, scope);

            scope.join();

            logger.info("Computation end!");

            return totalSum.sum();
        }
    }

    protected Void compute(List<Integer> intlist, StructuredTaskScope scope) {

        if (intlist.size() <= THRESHOLD) {

            segmentSum(intlist);

            return null;
        }

        int size = intlist.size();

        List<Integer> intlistLeft = intlist.subList(0, (size + 1) / 2);
        List<Integer> intlistRight = intlist.subList((size + 1) / 2, size);

        RecursiveScopedTask taskLeft = new RecursiveScopedTask(intlistLeft, 0, intlistLeft.size());       
        scope.fork(() -> taskLeft.compute(intlistLeft, StructuredTaskScope.open()));

        RecursiveScopedTask taskRight = new RecursiveScopedTask(intlistRight, 0, intlistRight.size());
        scope.fork(() -> taskRight.compute(intlistRight, StructuredTaskScope.open()));

        return null;
    }

    private Integer segmentSum(List<Integer> intlist) {

        int sum = intlist.stream()
                .reduce(0, Integer::sum);       

        totalSum.add(sum);
        
        // simulate the I/O-bound task part
        try { Thread.sleep((long) (Math.random() * 5000)); } catch (InterruptedException ex) {}

        logger.info(() -> "Thread: #" + Thread.currentThread().threadId() 
                + " Segment sum: " + intlist + " = " + sum);

        return sum;
    }
}