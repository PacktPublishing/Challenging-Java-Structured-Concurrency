package challenge.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final Random rnd = new Random();
    private static final int MAX_ELEMENTS = 50;

    public static void main(String[] args) {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

        List<Integer> intlist = new ArrayList<>();
        for (int i = 0; i < MAX_ELEMENTS; i++) {
            intlist.add(rnd.nextInt(10) + 1);
        }

        logger.info("Computation start!");
        ForkJoinSum forkJoinSum = new ForkJoinSum(intlist);
        Integer totalSum = forkJoinPool.invoke(forkJoinSum);
        logger.info("Computation end!");
        logger.info(() -> "Total sum: " + totalSum);
    }
}
