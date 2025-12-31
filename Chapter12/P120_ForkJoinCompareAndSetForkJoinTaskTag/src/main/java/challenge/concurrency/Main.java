package challenge.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
  
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

        Part partA = new Part("Part-A", new Unit(1));
        Part partB = new Part("Part-B", new Unit(2), partA);
        Part partC = new Part("Part-C", new Unit(3), partA, partB);
        Part partD = new Part("Part-D", new Unit(4), partA, partB, partC);

        logger.info(() -> "Unit value: " + forkJoinPool.invoke(partD));
    }

    private static class Unit implements Callable {

        private static final AtomicInteger unit = new AtomicInteger();
        private final Integer part;

        public Unit(Integer part) {
            this.part = part;
        }

        @Override
        public Integer call() {

            logger.info(() -> "Adding part: " + part
                    + " by thread:" + Thread.currentThread());

            try { Thread.sleep((long) (Math.random() * 500)); } catch (InterruptedException ex) {}

            return unit.addAndGet(part);
        }                     
    }
}
