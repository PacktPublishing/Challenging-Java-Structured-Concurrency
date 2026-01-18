package challenge.concurrency;

import java.lang.Thread.Builder.OfVirtual;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    private static final Thread.UncaughtExceptionHandler adminThreadExceptionHandler 
            = (Thread t, Throwable e) -> {
                logger.severe(() -> "Thread exception: " + t.getName());        
            };

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        // This is the task (job) executed by our threads
        Runnable voidTask = () -> logger.info(
                () -> "I'm the thread: " + Thread.currentThread().toString());
        
        // creating virtual threads via OfVirtual 
        OfVirtual virtualBuilder = Thread.ofVirtual(); 
        
        // here, set the properties common to all threads created via this virtual thread builder
        // virtualBuilder.name("adminVirtualThread"); // all threads have the same name, adminVirtualThread
        virtualBuilder.name("adminVirtualThread-", 0); // prefixed name, adminVirtualThread-0, adminVirtualThread-1, ...
        virtualBuilder.uncaughtExceptionHandler(adminThreadExceptionHandler);
           
        Thread virtualThread0 = virtualBuilder.start(voidTask);       
        Thread virtualThread1 = virtualBuilder.start(voidTask);
                
        logger.info("Before starting the virtual threads ...");                                                                                 

        virtualThread0.join();        
        virtualThread1.join();        

        logger.info("After the virtual threads has terminated ...");                                    
    }
}