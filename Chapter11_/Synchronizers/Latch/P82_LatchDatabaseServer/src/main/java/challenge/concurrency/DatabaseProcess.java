package challenge.concurrency;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

public class DatabaseProcess {
    
    private static final Logger logger = Logger.getLogger(DatabaseProcess.class.getName());
    
    private final String process;
    private final CountDownLatch cdl;

    public DatabaseProcess(String process, CountDownLatch cdl) {
        this.process = process;
        this.cdl = cdl;
    }        
        
    public void startProcess() {
        
        int startInAround = (int) (Math.random() * 10);

        try {
            logger.info(() -> "Starting database process: '" + process + "'");
            Thread.sleep(Duration.ofSeconds(startInAround));

            logger.info(() -> "Database process '" 
                    + process + "' started in " + startInAround + "s");                     
                        
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();                         
        } finally {
            cdl.countDown();            
        }        
    }    
}
