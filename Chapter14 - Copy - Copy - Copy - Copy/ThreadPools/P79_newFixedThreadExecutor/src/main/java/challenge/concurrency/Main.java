package challenge.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable cleanupLogs = ()
                -> logger.info(() -> "Clean logs:" + Thread.currentThread().toString());

        Runnable sortExceptions = ()
                -> logger.info(() -> "Sort exceptions:" + Thread.currentThread().toString());

        Runnable sortWarnings = ()
                -> logger.info(() -> "Sort warnings:" + Thread.currentThread().toString());
        
        Runnable highlightThresholds = ()
                -> logger.info(() -> "Highlight thresholds:" + Thread.currentThread().toString());

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {

            executor.submit(cleanupLogs);
            executor.submit(sortExceptions);
            executor.submit(sortWarnings);
            executor.submit(highlightThresholds);
        }
    }
}
