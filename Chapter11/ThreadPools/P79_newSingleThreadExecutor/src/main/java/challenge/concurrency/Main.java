package challenge.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable openConnection = ()
                -> logger.info(() -> "Connection ready:" + Thread.currentThread().toString());

        Runnable useConnection = ()
                -> logger.info(() -> "Connection in use:" + Thread.currentThread().toString());

        Runnable closeConnection = ()
                -> logger.info(() -> "Connection closed:" + Thread.currentThread().toString());

        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {

            executor.submit(openConnection);
            executor.submit(useConnection);
            executor.submit(closeConnection);
        }
    }
}
