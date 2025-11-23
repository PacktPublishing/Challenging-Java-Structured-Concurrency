package challenge.concurrency;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import static java.util.concurrent.StructuredTaskScope.Subtask.State.SUCCESS;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class DatabaseServer {

    private static final Logger logger = Logger.getLogger(DatabaseServer.class.getName());
    
    public void startDatabaseServer() throws InterruptedException {
        logger.info("Starting database server ...");
        logger.info("Initializing database processes ...\n");

        long startTime = System.nanoTime();

        try (var scope = StructuredTaskScope.open(Joiner.<String>awaitAll())) {
                        
            Subtask<String> s1 = scope.fork(() -> { new DatabaseProcess("Database Listeners").startProcess(); return "";});
            Subtask<String> s2 = scope.fork(() -> { new DatabaseProcess("Database Connection Pool").startProcess(); return "";});
            Subtask<String> s3 = scope.fork(() -> { new DatabaseProcess("Database Disk Space").startProcess(); return "";});
            
            scope.join();        
            
             if(s1.state().equals(SUCCESS) && s2.state().equals(SUCCESS) && s3.state().equals(SUCCESS)) {
                // latch success
            } else {
                // latch failed
                // handle FAILED subtasks
            }
        }                
        
         long endTime = System.nanoTime();

            logger.info(() -> "Database server started in "
                    + TimeUnit.NANOSECONDS.toSeconds(endTime - startTime) + " seconds");
    }
}