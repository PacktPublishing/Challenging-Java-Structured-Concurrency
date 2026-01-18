package challenge.concurrency;

import java.util.concurrent.Phaser;
import java.util.logging.Logger;

public class DatabaseServer {

    private static final Logger logger = Logger.getLogger(DatabaseServer.class.getName());

    private static final Phaser phaser = new Phaser(1) {

        @Override
        protected boolean onAdvance(int phase, int parties) {

            logger.info(() -> "Phase number:" + phase + " | " + parties);

            return parties == 0;
        }
    };

    public void startDatabaseServer() {
        logger.info("Starting database server ...");
        logger.info("Initializing database processes ...\n");
                 
        initDbServerFirstPhase();
        phaser.arriveAndAwaitAdvance();
        
        initDbServerSecondPhase();  
        phaser.arriveAndAwaitAdvance();
                
        initDbServerThirdPhase(); 
        phaser.arriveAndAwaitAdvance();                   
    }

    private void initDbServerFirstPhase() {                 
        
        DatabaseProcess p1 = new DatabaseProcess("Database Listeners", phaser);
        DatabaseProcess p2 = new DatabaseProcess("Database Connection Pool", phaser);
        DatabaseProcess p3 = new DatabaseProcess("Database Disk Space", phaser);
        
        Thread process1 = Thread.ofVirtual().unstarted(() -> p1.startProcess());
        Thread process2 = Thread.ofVirtual().unstarted(() -> p2.startProcess());
        Thread process3 = Thread.ofVirtual().unstarted(() -> p3.startProcess());

        process1.start();
        process2.start();
        process3.start();                                 
    }
    
    private void initDbServerSecondPhase() {               
    
        DatabaseProcess p4 = new DatabaseProcess("Database Connectors", phaser);
        DatabaseProcess p5 = new DatabaseProcess("Database Caching", phaser);
        
        Thread process4 = Thread.ofVirtual().unstarted(() -> p4.startProcess());
        Thread process5 = Thread.ofVirtual().unstarted(() -> p5.startProcess());        

        process4.start();
        process5.start();                     
    }
    
    private void initDbServerThirdPhase() {             
        
        DatabaseProcess p6 = new DatabaseProcess("Database Logging", phaser);

        Thread process6 = Thread.ofVirtual().unstarted(() -> p6.startProcess());
        
        process6.start();                      
    }
}