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
                
        Thread virtualThread1 = Thread.ofVirtual().name("vtThread#1").unstarted(voidTask);    
        Thread virtualThread2 = Thread.ofVirtual().name("vtThread#2").unstarted(voidTask);    
        Thread virtualThread3 = Thread.ofVirtual().name("vtThread#3").unstarted(voidTask);           
                
        logger.info("Before starting the virtual threads ...");                                                                                 

        // DON'T DO THIS
        // virtualThread1.start();        
        // virtualThread2.start();
        // virtualThread3.start();
        
        // virtualThread1.join();         
        // virtualThread2.join(); 
        // virtualThread3.join(); 
        
        // don't start thread 2 until thread 1 finishes
        virtualThread1.start();
        virtualThread1.join(); 
        
        // don't start thread 3 until thread 2 finishes
        virtualThread2.start();
        virtualThread2.join();   
        
        // finally start thread 3 and pause the main thread until it finishes
        virtualThread3.start();
        virtualThread3.join();                

        logger.info("After the virtual threads has terminated ...");
    }
}