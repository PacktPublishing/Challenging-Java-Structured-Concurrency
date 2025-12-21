package challenge.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;
 
public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final int TASK_NR = 10;

    static class VtPtThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
           // return Thread.ofPlatform().unstarted(r); // platform thread
           return Thread.ofVirtual().unstarted(r);     // virtual thread
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable voidTask = () -> logger.info(Thread.currentThread().toString());

        Callable<Boolean> returnTask = () -> {
            logger.info(Thread.currentThread().toString());
            return Math.random() < 0.5d;
        };

        logger.info(() -> "\nUsing the virtual thread executor and Runnable");
        try (ExecutorService executor = Executors
                .newVirtualThreadPerTaskExecutor()) {

            for (int i = 0; i < TASK_NR; i++) {

                executor.submit(voidTask);
                // Future<?> future = executor.submit(voidTask);                
            }
        }

        logger.info(() -> "\nUsing the virtual thread executor and Callable");
        try (ExecutorService executor = Executors
                .newVirtualThreadPerTaskExecutor()) {

            for (int i = 0; i < TASK_NR; i++) {

                executor.submit(returnTask);
                // Future<Boolean> future = executor.submit(returnTask);
            }
        }

        logger.info(() -> "\nUsing the platform thread executor and Runnable");
        VtPtThreadFactory vtPtFactory = new VtPtThreadFactory();
        try (ExecutorService executor = Executors
                .newThreadPerTaskExecutor(vtPtFactory)) {

            for (int i = 0; i < TASK_NR; i++) {

                executor.submit(voidTask);
                // Future<?> future = executor.submit(voidTask);
            }
        }

        logger.info(() -> "\nUsing the platform thread executor and Callable");
        try (ExecutorService executor = Executors
                .newThreadPerTaskExecutor(vtPtFactory)) {

            for (int i = 0; i < TASK_NR; i++) {

                executor.submit(returnTask);
                // Future<Boolean> future = executor.submit(returnTask);
            }
        }
    }
}