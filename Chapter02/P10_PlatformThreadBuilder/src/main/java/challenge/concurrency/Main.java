package challenge.concurrency;

import java.lang.Thread.Builder.OfPlatform;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        // This is the task (job) executed by the platform thread
        Runnable voidTask = () -> logger.info(
                () -> "I'm a platform thread: " + Thread.currentThread().toString());
        
        // creating platform threads via OfPlatform 
        OfPlatform platformBuilder = Thread.ofPlatform(); 
        
        // here, set the properties common to all threads created via this platform thread builder
                
        logger.info("Before starting the platform threads (1 and 2) ...");                                                                                 
        
        Thread platformThread1 = platformBuilder.start(voidTask);
        Thread platformThread2 = platformBuilder.start(voidTask);                       

        platformThread1.join();        
        platformThread2.join();        

        logger.info("After the platform threads (1 and 2) has terminated ...");
        
        logger.info("----------------------------");
        
        logger.info("Before starting the platform threads (3 and 4) ...");                                                                                 
        
        // chaining OfPlatform in a single line of code
        Thread platformThread3 = Thread.ofPlatform().start(voidTask);
        Thread platformThread4 = Thread.ofPlatform().start(voidTask);
                
        platformThread3.join();        
        platformThread4.join();        

        logger.info("After the platform threads (3 and 4) has terminated ...");     
        
        logger.info("----------------------------");
        
        // creating unstarted platform threads
        logger.info("Before creating the unstarted platform threads (5, 6, 7, and 8) ...");
        
        Thread platformThread5 = platformBuilder.unstarted(voidTask);
        Thread platformThread6 = platformBuilder.unstarted(voidTask);
        
        Thread platformThread7 = Thread.ofPlatform().unstarted(voidTask);
        Thread platformThread8 = Thread.ofPlatform().unstarted(voidTask);
        
        logger.info("Before starting the unstarted platform threads (5, 6, 7, and 8) ...");
        
        logger.info( () -> "platformThread5 is alive ? " + platformThread5.isAlive() 
                + " | platformThread6 is alive ? " + platformThread6.isAlive()
                + " | platformThread7 is alive ? " + platformThread7.isAlive()
                + " | platformThread8 is alive ? " + platformThread8.isAlive());
        
        // start the platform threads
        platformThread5.start();
        platformThread6.start();
        platformThread7.start();
        platformThread8.start();
        
        // you'll see "true" only for those threads that haven't complete yet
        logger.info("After starting the platform threads (5, 6, 7, and 8) ...");
        logger.info( () -> "platformThread5 is alive ? " + platformThread5.isAlive() 
                + " | platformThread6 is alive ? " + platformThread6.isAlive()
                + " | platformThread7 is alive ? " + platformThread7.isAlive()
                + " | platformThread8 is alive ? " + platformThread8.isAlive());
        
        platformThread5.join();
        platformThread6.join();
        platformThread7.join();
        platformThread8.join();    
        
        logger.info("After the platform threads (5, 6, 7, and 8) has terminated ...");     
        logger.info( () -> "platformThread5 is alive ? " + platformThread5.isAlive() 
                + " | platformThread6 is alive ? " + platformThread6.isAlive()
                + " | platformThread7 is alive ? " + platformThread7.isAlive()
                + " | platformThread8 is alive ? " + platformThread8.isAlive());
    }
}