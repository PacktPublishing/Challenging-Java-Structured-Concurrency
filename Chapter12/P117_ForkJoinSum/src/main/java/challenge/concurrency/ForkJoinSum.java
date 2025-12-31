package challenge.concurrency;

import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.logging.Logger;

public class ForkJoinSum extends RecursiveTask<Integer> {

    private static final Logger logger = Logger.getLogger(ForkJoinSum.class.getName());
    private static final int THRESHOLD = 10;

    private final List<Integer> intlist;

    public ForkJoinSum(List<Integer> intlist) {
        this.intlist = intlist;
    }

    @Override
    protected Integer compute() {
        if (intlist.size() <= THRESHOLD) {
            return segmentSum(intlist);
        }

        int size = intlist.size();

        List<Integer> intlistLeft = intlist.subList(0, (size + 1) / 2);
        List<Integer> intlistRight = intlist.subList((size + 1) / 2, size);

        ForkJoinSum taskLeft = new ForkJoinSum(intlistLeft);
        ForkJoinSum taskRight = new ForkJoinSum(intlistRight);
        
        taskLeft.fork();  
        taskRight.fork();

        Integer sumTaskRight = taskRight.join();
        Integer sumTaskLeft = taskLeft.join();

        return sumTaskLeft + sumTaskRight;
    }

    private Integer segmentSum(List<Integer> intlist) {

        int sum = intlist.stream()
                .reduce(0, Integer::sum);
        
        // simulate the I/O-bound task part
        try { Thread.sleep((long) (Math.random() * 5000)); } catch (InterruptedException ex) {}

        logger.info(() -> "Thread: #" + Thread.currentThread().threadId()
                + " Segment sum: " + intlist + " = " + sum);

        return sum;
    }
}
