package challenge.concurrency;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        // This is the task (job) executed by the virtual thread
        Runnable voidTask = () -> logger.info(
                () -> "I'm the thread: " + Thread.currentThread().toString());
                
        Thread virtualThread = Thread.ofVirtual().unstarted(voidTask);                       
       
        logger.info("Before starting the virtual thread ..."); 
        virtualThread.start();  
        virtualThread.join();
        logger.info("After the virtual thread has terminated ...");                                
    }
}