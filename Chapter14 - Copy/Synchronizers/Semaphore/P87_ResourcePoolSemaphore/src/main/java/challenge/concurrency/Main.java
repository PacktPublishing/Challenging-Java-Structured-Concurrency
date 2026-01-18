package challenge.concurrency;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
  
    public static void main(String[] args) {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        // we have a database connection pool of 5 connections
        LimitedResource<String> dbConnections = new LimitedResource(
                5, List.of("Db-Conn-1", "Db-Conn-2", "Db-Conn-3", "Db-Conn-4", "Db-Conn-5"));

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            for (int i = 0; i < 10; i++) {
                executor.submit(() -> {
                    try {
                        // open database connection
                        String connection = dbConnections.open();
                        logger.info(() -> "Database connection opened: " + connection
                                + " by " + Thread.currentThread());

                        // use databse connection
                        logger.info(() -> "Using database connection: " + connection
                                + " by " + Thread.currentThread());
                        Thread.sleep((long) (Math.random() * 1000));

                        // close database connection
                        dbConnections.close(connection);
                        logger.info(() -> "Database connection closed: " + connection
                                + " by " + Thread.currentThread());
                    } catch (InterruptedException ex) {}
                });
            }
        }
    }
}
