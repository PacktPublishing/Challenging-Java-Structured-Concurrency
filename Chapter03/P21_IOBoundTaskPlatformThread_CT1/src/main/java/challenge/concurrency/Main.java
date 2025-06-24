package challenge.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final int TASKS_NR = Runtime.getRuntime().availableProcessors();

    private static void ioBoundTask() { 
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

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
       
        long startTime = System.nanoTime();
        try (ExecutorService executor // switch on newVirtualThreadPerTaskExecutor() and interpret the results
                = Executors.newThreadPerTaskExecutor(Executors.defaultThreadFactory())) { 
            for (int i = 0; i < TASKS_NR; i++) {
                int ci = i; 
                executor.submit(() -> {
                    ioBoundTask();
                    logger.info(() -> "Task time ioBoundTask (s): " 
                        + TimeUnit.NANOSECONDS.toSeconds((
                                System.nanoTime() - startTime - TimeUnit.SECONDS.toNanos(ci))));            
                });
                
                Thread.sleep(1000); // give some time between submits
            }
        }
    }
}