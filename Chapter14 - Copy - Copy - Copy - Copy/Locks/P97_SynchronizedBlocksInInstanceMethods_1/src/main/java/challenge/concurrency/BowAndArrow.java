package challenge.concurrency;

import java.util.logging.Logger;

public class BowAndArrow {

    private static final Logger logger = Logger.getLogger(BowAndArrow.class.getName());

    public int arrows = 10;   

    public void shoot() {
        
        // non-synchronized code
        arrows--;
        logger.info(() -> "Shot by " + Thread.currentThread().getName());        

        synchronized (this) {
            logger.info(() -> "Shot by " + Thread.currentThread().getName());
            arrows--;            
        }
        
        // non-synchronized code
        arrows--; 
        logger.info(() -> "Shot by " + Thread.currentThread().getName());        
    }
}