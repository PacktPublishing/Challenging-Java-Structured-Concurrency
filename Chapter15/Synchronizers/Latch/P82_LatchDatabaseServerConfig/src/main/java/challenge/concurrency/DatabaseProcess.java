package challenge.concurrency;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

public class DatabaseProcess {
    
    private static final Logger logger = Logger.getLogger(DatabaseProcess.class.getName());
    
    private final String process;
    private final CountDownLatch cdlServer;
    private final CountDownLatch cdlConfig;

    public DatabaseProcess(String process, CountDownLatch cdlServer, CountDownLatch cdlConfig) {
        this.process = process;
        this.cdlServer = cdlServer;
        this.cdlConfig = cdlConfig;
    }        
        
    public void startProcess() {                                

        try {
            cdlConfig.await(); // wait for configuration to be complete
            
            int startInAround = (int) (Math.random() * 10);                        
            
            logger.info(() -> "Starting database process: '" + process + "'");
            Thread.sleep(Duration.ofSeconds(startInAround));

            logger.info(() -> "Database process '" 
                    + process + "' started in " + startInAround + "s");                     
                        
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();                         
        } finally {
            cdlServer.countDown();            
        }        
    }    
}
