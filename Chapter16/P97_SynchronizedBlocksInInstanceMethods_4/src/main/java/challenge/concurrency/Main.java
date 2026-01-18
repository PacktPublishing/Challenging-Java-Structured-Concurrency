package challenge.concurrency;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
  
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
         
        Quiver quiver1 = new Quiver("BulletTip", 10);
        Quiver quiver2 = new Quiver("BluntTip", 10);
        BowAndArrow bow = new BowAndArrow(quiver1, quiver2);
        
        Thread t1 = Thread.ofVirtual().name("archer-1").unstarted(() -> bow.shoot1());
        Thread t2 = Thread.ofVirtual().name("archer-2").unstarted(() -> bow.shoot2());
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        
        logger.info(() -> "Bow arrows left (quiver 1): " + bow.quiver1.arrows);   
        logger.info(() -> "Bow arrows left (quiver 2): " + bow.quiver2.arrows);   
    }
}
