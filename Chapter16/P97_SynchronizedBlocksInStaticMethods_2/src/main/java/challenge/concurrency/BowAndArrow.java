package challenge.concurrency;

import java.util.logging.Logger;

public class BowAndArrow {

    private static final Logger logger = Logger.getLogger(BowAndArrow.class.getName());

    public static final Quiver quiver = new Quiver("BulletTip", 10);
    
    public static void shoot1() {
       
        synchronized (quiver) {
            logger.info(() -> "Shot by " + Thread.currentThread().getName());
            quiver.arrows--;              
        }
    }
    
    public static void shoot2() {
       
        synchronized (quiver) {
            logger.info(() -> "Shot by " + Thread.currentThread().getName());
            quiver.arrows--;            
        }
    }
}
