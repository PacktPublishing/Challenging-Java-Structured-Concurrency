package challenge.concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class DatabaseServer {
    
    static class Configuration {
       static void initConfig() {
           logger.info("Database server configuration ...");
       }
       
       static void polishConfig() {
           logger.info("Polishing configuration ...");
       }
    }

    private static final Logger logger = Logger.getLogger(DatabaseServer.class.getName());

    private final CountDownLatch cdlServer = new CountDownLatch(3);
    private final CountDownLatch cdlConfig = new CountDownLatch(1);
   
    public void startDatabaseServer() {
        logger.info("Starting database server ...");
        logger.info("Initializing database processes ...\n");

        long startTime = System.nanoTime();

        DatabaseProcess p1 = new DatabaseProcess("Database Listeners", cdlServer, cdlConfig);
        DatabaseProcess p2 = new DatabaseProcess("Database Connection Pool", cdlServer, cdlConfig);
        DatabaseProcess p3 = new DatabaseProcess("Database Disk Space", cdlServer, cdlConfig);
        
        Thread process1 = Thread.ofVirtual().unstarted(() -> p1.startProcess());
        Thread process2 = Thread.ofVirtual().unstarted(() -> p2.startProcess());
        Thread process3 = Thread.ofVirtual().unstarted(() -> p3.startProcess());

        process1.start();
        process2.start();
        process3.start();

        try {
            Configuration.initConfig();   // configure server
            cdlConfig.countDown();        // allow server to start
            Configuration.polishConfig(); // while server is starting polish configuration
            cdlServer.await();            // wait for server to be started

            long endTime = System.nanoTime();

            logger.info(() -> "Database server started in "
                    + TimeUnit.NANOSECONDS.toSeconds(endTime - startTime) + "s");
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();            
        }
    }
}
