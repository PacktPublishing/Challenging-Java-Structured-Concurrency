package challenge.concurrency;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.LongAdder;
import java.util.logging.Logger;

record RecursiveCompletableFuture(List<Integer> intlist, int start, int end) {

    private static final Logger logger = Logger.getLogger(RecursiveCompletableFuture.class.getName());

    private static final int THRESHOLD = 10;
    private static final LongAdder totalSum = new LongAdder();

    public static long sumUp(List<Integer> intlist, int start, int end) {
        
        logger.info("Computation start!");

        RecursiveCompletableFuture.execute(intlist, 0, intlist.size()).join();
        
        logger.info("Computation end!");

        return totalSum.sum();
    }
    
    public static CompletableFuture<Void>
            execute(List<Integer> intlist, int start, int end) {
        
        return CompletableFuture.runAsync(() -> 
                new RecursiveCompletableFuture(intlist, start, end).compute(intlist),
                Thread::startVirtualThread);
    }

    protected void compute(List<Integer> intlist) {

        if (intlist.size() <= THRESHOLD) {

            segmentSum(intlist);
            
            return;           
        }

        int size = intlist.size();

        List<Integer> intlistLeft = intlist.subList(0, (size + 1) / 2);
        List<Integer> intlistRight = intlist.subList((size + 1) / 2, size);

        CompletableFuture<Void> leftList = execute(intlistLeft, 0, intlistLeft.size());
        CompletableFuture<Void> rightList = execute(intlistRight, 0, intlistRight.size());
        
        leftList.join();
        rightList.join();
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