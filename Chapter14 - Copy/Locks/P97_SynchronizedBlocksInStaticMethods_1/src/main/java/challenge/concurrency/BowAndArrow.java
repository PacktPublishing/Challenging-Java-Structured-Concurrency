package challenge.concurrency;

import java.util.logging.Logger;

public class BowAndArrow {

    private static final Logger logger = Logger.getLogger(BowAndArrow.class.getName());
   
    public static int arrows = 10;
      
    public static void shoot1() {
       
        synchronized (BowAndArrow.class) {
            logger.info(() -> "Shot by " + Thread.currentThread().getName());
            arrows--;                 
        }
    }
    
    public static void shoot2() {
       
        synchronized (BowAndArrow.class) {
            logger.info(() -> "Shot by " + Thread.currentThread().getName());
            arrows--;            
        }
    }
}
