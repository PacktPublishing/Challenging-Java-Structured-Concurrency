package challenge.concurrency;

import java.time.Duration;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        // This is the task (job) executed by the virtual thread
        Runnable voidTask = () -> logger.info(
                () -> "I'm a virtual thread: " + Thread.currentThread().toString());
                
        Thread virtualThread1 = Thread.ofVirtual().unstarted(voidTask);    
        Thread virtualThread2 = Thread.ofVirtual().unstarted(voidTask);
        Thread virtualThread3 = Thread.ofVirtual().unstarted(voidTask);
        Thread virtualThread4 = Thread.ofVirtual().unstarted(voidTask);  
                
        logger.info("Before starting the virtual threads ...");                                                                                 

        virtualThread1.start();
        virtualThread2.start();
        virtualThread3.start();
        virtualThread4.start();
        
        virtualThread1.join();        
        virtualThread2.join(Duration.ofNanos(100));        
        virtualThread3.join(10);        
        virtualThread4.join(10, 100);

        logger.info("After the virtual threads has terminated ...");
    }
}