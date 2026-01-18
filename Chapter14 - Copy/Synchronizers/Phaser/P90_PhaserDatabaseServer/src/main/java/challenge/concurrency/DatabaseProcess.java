package challenge.concurrency;

import java.time.Duration;
import java.util.concurrent.Phaser;
import java.util.logging.Logger;

public class DatabaseProcess {
    
    private static final Logger logger = Logger.getLogger(DatabaseProcess.class.getName());
    
    private final String process;
    private final Phaser phaser;

    public DatabaseProcess(String process, Phaser phaser) {
        this.process = process;
        this.phaser = phaser;
                
        this.phaser.register();
    }        
        
    public void startProcess() {                 
        
        int startInAround = (int) (Math.random() * 10);

        try {
            logger.info(() -> "Starting database process: '" + process + "'");
            Thread.sleep(Duration.ofSeconds(startInAround));

            logger.info(() -> "Database process '" 
                    + process + "' started in " + startInAround + " seconds");
                                    
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();                         
        } finally {            
            phaser.arriveAndDeregister();
        }        
    }    
}
