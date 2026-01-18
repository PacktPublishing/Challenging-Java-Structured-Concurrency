package challenge.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable cpuBoundShortTask = ()
                -> logger.info(() -> "CPU bound short task:" + Thread.currentThread().toString());

        try (ExecutorService executor = Executors.newWorkStealingPool()) {

            for (int i = 0; i < 10_000; i++) {
                executor.submit(cpuBoundShortTask);
            }
        }
    }
}
