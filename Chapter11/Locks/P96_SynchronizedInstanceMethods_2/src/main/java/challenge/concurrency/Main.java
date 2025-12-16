package challenge.concurrency;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
  
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        BowAndArrow bow = new BowAndArrow();        
        
        Thread t1 = Thread.ofVirtual().name("archer-1").unstarted(() -> bow.shoot());
        Thread t2 = Thread.ofVirtual().name("archer-2").unstarted(() -> bow.load());
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        
        logger.info(() -> "Bow arrows left: " + bow.arrows);       
    }
}
