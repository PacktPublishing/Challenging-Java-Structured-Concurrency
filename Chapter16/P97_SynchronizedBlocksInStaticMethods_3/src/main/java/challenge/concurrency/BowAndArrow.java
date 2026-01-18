package challenge.concurrency;

import java.util.logging.Logger;

public class BowAndArrow {

    private static final Logger logger = Logger.getLogger(BowAndArrow.class.getName());

    public static final Quiver quiver1 = new Quiver("BulletTip", 10);
    public static final Quiver quiver2 = new Quiver("BluntTip", 10);
    
    public static void shoot1() {
       
        synchronized (quiver1) {
            logger.info(() -> "Shot by " + Thread.currentThread().getName());
            quiver1.arrows--;             
        }
    }
    
    public static void shoot2() {
       
        synchronized (quiver2) {
            logger.info(() -> "Shot by " + Thread.currentThread().getName());
            quiver2.arrows--;         
        }
    }
}
