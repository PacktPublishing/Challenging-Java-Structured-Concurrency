package challenge.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.StructuredTaskScope;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        logger.info("Executing the non-atomic task via executor: ");
        NonAtomicIncRunnable naiRunnableTask = new NonAtomicIncRunnable();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            for (int i = 0; i < 1_000_000; i++) {
                executor.submit(naiRunnableTask);
            }
        }
        logger.info(() -> "Non-atomic task result via executor: " + naiRunnableTask.getCounter());
       
        logger.info("Executing the non-atomic task via STS: ");
        NonAtomicIncCallable naiCallableTask = new NonAtomicIncCallable();
        try (StructuredTaskScope scope = new StructuredTaskScope<Integer>()) {

            for (int i = 0; i < 1_000_000; i++) {                
                scope.fork(naiCallableTask);
            }
            
            scope.join();
        }
        logger.info(() -> "Non-atomic task result via STS: " + naiCallableTask.getCounter());

        logger.info("Executing the sync task via executor: ");
        SyncAtomicInc saiTask1 = new SyncAtomicInc();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            for (int i = 0; i < 1_000_000; i++) {
                executor.submit(() -> saiTask1.inc());
            }
        }
        logger.info(() -> "Sync task result via executor: " + saiTask1.getCounter());
        
        logger.info("Executing the sync task via STS: ");
        SyncAtomicInc saiTask2 = new SyncAtomicInc();
        try (StructuredTaskScope scope = new StructuredTaskScope<Integer>()) {

            for (int i = 0; i < 1_000_000; i++) {
                scope.fork(() -> saiTask2.inc());
            }
            
            scope.join();
        }
        logger.info(() -> "Sync task result via STS: " + saiTask2.getCounter());

        logger.info("Executing the atomic task via executor: ");
        AtomicIncRunnable aiRunnableTask = new AtomicIncRunnable();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            for (int i = 0; i < 1_000_000; i++) {
                executor.submit(aiRunnableTask);
            }
        }
        logger.info(() -> "Atomic task result via STS: " + aiRunnableTask.getCounter());
        
        logger.info("Executing the atomic task via STS: ");
        AtomicIncCallable aiCallableTask = new AtomicIncCallable();
        try (StructuredTaskScope scope = new StructuredTaskScope<Integer>()) {

            for (int i = 0; i < 1_000_000; i++) {
                scope.fork(aiCallableTask);
            }
            
            scope.join();
        }
        logger.info(() -> "Atomic task result via STS: " + aiCallableTask.getCounter());
    }
}