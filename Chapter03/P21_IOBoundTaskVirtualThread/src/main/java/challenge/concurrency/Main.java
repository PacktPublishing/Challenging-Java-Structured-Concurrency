package challenge.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
   
    private static void mainIoBoundTask() { // 1
        logger.info(() -> "1: " + Thread.currentThread().toString());
        logger.info(() -> "1 [#" + Thread.currentThread().threadId() + "] : Database query ----> ");
        try { Thread.sleep(3000); } catch (InterruptedException ex) {}
        logger.info(() -> "1 [#" + Thread.currentThread().threadId() + "] : Result set <----");

        logger.info(() -> "1 [#" + Thread.currentThread().threadId() + "] : Service request ----> ");
        try { Thread.sleep(2000); } catch (InterruptedException ex) {}
        logger.info(() -> "1 [#" + Thread.currentThread().threadId() + "] : JSON response <----");

        logger.info(() -> "1 [#" + Thread.currentThread().threadId() + "] : Print report ----> ");
        try { Thread.sleep(1000); } catch (InterruptedException ex) {}
        logger.info(() -> "1 [#" + Thread.currentThread().threadId() + "] : 200 OK <----");
    }

    private static void otherIoBoundTask() { // 2
        logger.info(() -> "2: " + Thread.currentThread().toString());
        logger.info(() -> "2 [#" + Thread.currentThread().threadId() + "] : Update data ----> ");
        try { Thread.sleep(4000); } catch (InterruptedException ex) {}
        logger.info(() -> "2 [#" + Thread.currentThread().threadId() + "] : Data update done <----");
    }

    public static void main(String[] args) {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        System.setProperty("jdk.virtualThreadScheduler.parallelism", "1");
        System.setProperty("jdk.virtualThreadScheduler.maxPoolSize", "1");
        System.setProperty("jdk.virtualThreadScheduler.minRunnable", "1");
        
        long startTime = System.nanoTime();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(() -> {
                mainIoBoundTask();
                logger.info(() -> "Task time mainIoBoundTask (s): "
                        + TimeUnit.NANOSECONDS.toSeconds((System.nanoTime() - startTime)));
            });
            executor.submit(() -> {
                otherIoBoundTask();
                logger.info(() -> "Task time otherIoBoundTask (s): "
                        + TimeUnit.NANOSECONDS.toSeconds((System.nanoTime() - startTime)));
            });
        }
    }
}
