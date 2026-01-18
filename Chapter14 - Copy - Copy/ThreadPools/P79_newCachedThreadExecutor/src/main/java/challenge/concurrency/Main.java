package challenge.concurrency;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable shortLivedTask = ()
                -> logger.info(Thread.currentThread().toString());

        try (ExecutorService executor = Executors.newCachedThreadPool()) {

            // First 200 tasks arrives under 50 ms
            for (int i = 0; i < 200; i++) {
                Thread.sleep(Duration.ofMillis((long) (Math.random() * 50)));
                executor.submit(shortLivedTask);
            }

            // Next 500 tasks arrives very fast - expand on-demand
            for (int i = 0; i < 500; i++) {
                executor.submit(shortLivedTask);
            }

            // Last 300 tasks arrives in 2-3 seconds - contract on-demand
            for (int i = 0; i < 300; i++) {                                                            
                Thread.sleep(Duration.ofSeconds((long) (2 + Math.random())));
                executor.submit(shortLivedTask);
            }
        }
    }
}