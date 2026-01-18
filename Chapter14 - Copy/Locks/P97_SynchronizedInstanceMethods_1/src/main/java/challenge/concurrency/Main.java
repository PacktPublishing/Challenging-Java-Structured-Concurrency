package challenge.concurrency;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
  
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        BowAndArrow bow1 = new BowAndArrow();
        BowAndArrow bow2 = new BowAndArrow();
        
        Thread t1 = Thread.ofVirtual().name("archer-1").unstarted(() -> bow1.shoot());
        Thread t2 = Thread.ofVirtual().name("archer-2").unstarted(() -> bow2.shoot());
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        
        logger.info(() -> "Bow 1 arrows left: " + bow1.arrows);
        logger.info(() -> "Bow 2 arrows left: " + bow2.arrows);
    }
}
