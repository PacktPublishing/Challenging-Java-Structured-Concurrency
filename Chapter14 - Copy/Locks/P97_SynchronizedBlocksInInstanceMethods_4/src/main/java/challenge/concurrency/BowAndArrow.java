package challenge.concurrency;

import java.util.logging.Logger;

public class BowAndArrow {

    private static final Logger logger = Logger.getLogger(BowAndArrow.class.getName());

    public final Quiver quiver1;   
    public final Quiver quiver2;   

    BowAndArrow(Quiver quiver1,Quiver quiver2) {
        this.quiver1 = quiver1;
        this.quiver2 = quiver2;
    }

    public void shoot1() {
       
        synchronized (quiver1) {
            logger.info(() -> "Shot by " + Thread.currentThread().getName());
            quiver1.arrows--;                 
        }
    }
    
    public void shoot2() {
       
        synchronized (quiver2) {
            logger.info(() -> "Shot by " + Thread.currentThread().getName());
            quiver2.arrows--;             
        }
    }
}
