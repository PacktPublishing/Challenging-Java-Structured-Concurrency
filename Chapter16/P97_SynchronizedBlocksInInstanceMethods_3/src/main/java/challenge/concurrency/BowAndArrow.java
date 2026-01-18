package challenge.concurrency;

import java.util.logging.Logger;

public class BowAndArrow {

    private static final Logger logger = Logger.getLogger(BowAndArrow.class.getName());

    public final Quiver quiver;   

    BowAndArrow(Quiver quiver) {
        this.quiver = quiver;        
    }

    public void shoot1() {
       
        synchronized (quiver) {
            logger.info(() -> "Shot by " + Thread.currentThread().getName());
            quiver.arrows--;                        
        }
    }
    
    public void shoot2() {
       
        synchronized (quiver) {
            logger.info(() -> "Shot by " + Thread.currentThread().getName());
            quiver.arrows--;            
        }
    }
}
