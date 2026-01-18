package challenge.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import static java.util.concurrent.Future.State.SUCCESS;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class DatabaseServer {

    private static final Logger logger = Logger.getLogger(DatabaseServer.class.getName());

    public void startDatabaseServer() throws InterruptedException {
        logger.info("Starting database server ...");
        logger.info("Initializing database processes ...\n");

        long startTime = System.nanoTime();

        Future<?> f1, f2, f3;
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            f1 = executor.submit(() -> new DatabaseProcess("Database Listeners").startProcess());
            f2 = executor.submit(() -> new DatabaseProcess("Database Connection Pool").startProcess());
            f3 = executor.submit(() -> new DatabaseProcess("Database Disk Space").startProcess());
        }
        
        if (f1.state().equals(SUCCESS) && f2.state().equals(SUCCESS) && f3.state().equals(SUCCESS)) {
            // latch success
        } else {
            // latch failed
            // handle FAILED futures
        }

        long endTime = System.nanoTime();

        logger.info(() -> "Database server started in "
                + TimeUnit.NANOSECONDS.toSeconds(endTime - startTime) + " seconds");
    }
}
