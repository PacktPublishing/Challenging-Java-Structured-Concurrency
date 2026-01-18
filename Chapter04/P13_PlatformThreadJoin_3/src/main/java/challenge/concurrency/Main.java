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
                
        Thread platformThread1 = Thread.ofPlatform().name("ptThread#1").unstarted(voidTask);    
        Thread platformThread2 = Thread.ofPlatform().name("ptThread#2").unstarted(voidTask);    
        Thread platformThread3 = Thread.ofPlatform().name("ptThread#3").unstarted(voidTask);           
                
        logger.info("Before starting the platform threads ...");                                                                                 

        // DON'T DO THIS
        // platformThread1.start();        
        // platformThread2.start();
        // platformThread3.start();               
        
        // don't start thread 2 until thread 1 finishes
        platformThread1.start();
        platformThread1.join(); 
        
        // don't start thread 3 until thread 2 finishes
        platformThread2.start();
        platformThread2.join();   
        
        // finally start thread 3 and pause the main thread until it finishes
        platformThread3.start();
        platformThread3.join();        

        logger.info("After the platform threads has terminated ...");
    }
}