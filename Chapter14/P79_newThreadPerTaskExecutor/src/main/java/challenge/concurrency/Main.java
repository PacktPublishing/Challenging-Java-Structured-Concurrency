package challenge.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable cpuBoundTask = () -> {            
            logger.info(() -> "CPU-bound task:" + Thread.currentThread().toString());
        };

        long startTime = System.nanoTime();

        try (ExecutorService executor = Executors
                .newThreadPerTaskExecutor(Executors.defaultThreadFactory())) {

            for (int i = 0; i < 10_000; i++) {
                executor.submit(cpuBoundTask);
            }
        }

        long endTime = System.nanoTime();

        logger.info(() -> "Tasks completed in: "
                + ((endTime - startTime) / 1_000) + " ms");
    }
}
