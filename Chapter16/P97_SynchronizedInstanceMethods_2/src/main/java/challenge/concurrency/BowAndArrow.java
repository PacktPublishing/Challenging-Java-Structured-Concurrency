package challenge.concurrency;

import java.util.logging.Logger;

public class BowAndArrow {
    
    private static final Logger logger = Logger.getLogger(BowAndArrow.class.getName());
    
    public int arrows = 10;
    
    public synchronized void shoot() {
        
        logger.info(() -> "Shot by " + Thread.currentThread().getName());
        arrows--;        
    } 
    
    public synchronized void load() {
        
        logger.info(() -> "Load by " + Thread.currentThread().getName());
        arrows++;
    } 
}
