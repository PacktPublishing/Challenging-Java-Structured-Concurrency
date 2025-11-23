package challenge.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");                        
        
        logger.info("Executing the atomic task via incrementAndGet(): ");
        AtomicIncrementAndGet aigTask = new AtomicIncrementAndGet();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            
            for(int i = 0; i < 1_000_000; i++) {
                executor.submit(aigTask);
            }
        }
        logger.info(() -> "Atomic task result via incrementAndGet(): " + aigTask.getCounter());
        
        logger.info("Executing the atomic task via compareAndSet(): ");
        AtomicCompareAndSet casTask = new AtomicCompareAndSet();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            
            for(int i = 0; i < 1_000_000; i++) {
                executor.submit(casTask);
            }
        }
        logger.info(() -> "Atomic task result via compareAndSet(): " + casTask.getCounter());
    }
}
