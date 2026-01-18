package challenge.concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class DatabaseServer {

    private static final Logger logger = Logger.getLogger(DatabaseServer.class.getName());

    private final CountDownLatch cdl = new CountDownLatch(3);
   
    public void startDatabaseServer() {
        logger.info("Starting database server ...");
        logger.info("Initializing database processes ...\n");

        long startTime = System.nanoTime();

        DatabaseProcess p1 = new DatabaseProcess("Database Listeners", cdl);
        DatabaseProcess p2 = new DatabaseProcess("Database Connection Pool", cdl);
        DatabaseProcess p3 = new DatabaseProcess("Database Disk Space", cdl);
        
        Thread process1 = Thread.ofVirtual().unstarted(() -> p1.startProcess());
        Thread process2 = Thread.ofVirtual().unstarted(() -> p2.startProcess());
        Thread process3 = Thread.ofVirtual().unstarted(() -> p3.startProcess());

        process1.start();
        process2.start();
        process3.start();

        try {
            cdl.await();

            long endTime = System.nanoTime();

            logger.info(() -> "Database server started in "
                    + TimeUnit.NANOSECONDS.toSeconds(endTime - startTime) + "s");
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();            
        }
    }
}
