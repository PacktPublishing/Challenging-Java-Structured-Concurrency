package challenge.concurrency;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable stopDownload = ()
                -> logger.info(() -> "Download stopped:" + Thread.currentThread().toString());
        Runnable startUpload = ()
                -> logger.info(() -> "Upload started:" + Thread.currentThread().toString());

        try (ScheduledExecutorService executor = Executors.newScheduledThreadPool(2)) {

            executor.schedule(stopDownload, 10, TimeUnit.SECONDS);
            executor.schedule(startUpload, 12, TimeUnit.SECONDS);
        }
    }
}
