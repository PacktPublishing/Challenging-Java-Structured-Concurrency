package challenge.concurrency;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        // This is the task (job) executed by the virtual thread
        Runnable voidTask = () -> logger.info(Thread.currentThread().toString()); // 3-log
                             
        Thread virtualThread = Thread.ofVirtual().name("vtThread").unstarted(voidTask);                
        
        logger.info(virtualThread.toString()); // 1-log
        
        virtualThread.start();
        logger.info(virtualThread.toString()); // 2-log
        
        virtualThread.join();     
        logger.info(virtualThread.toString()); // 4-log
    }
}