package challenge.concurrency;

import java.lang.Thread.Builder.OfPlatform;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    private static final Thread.UncaughtExceptionHandler adminThreadExceptionHandler 
            = (Thread t, Throwable e) -> {
                logger.severe(() -> "Thread exception: " + t.getName());        
            };
    
    private static final ThreadGroup adminPtThreadGroup = new ThreadGroup("AdminPlatformThreads");

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        // This is the task (job) executed by our threads
        Runnable voidTask = () -> logger.info(
                () -> "I'm the thread: " + Thread.currentThread().toString());
        
        // creating platform threads via OfPlatform 
        OfPlatform platformBuilder = Thread.ofPlatform(); 
        
        // here, set the properties common to all threads created via this platform thread builder
        platformBuilder.name("adminPlatformThread"); // all threads have the same name, adminPlatformThread
        platformBuilder.name("adminPlatformThread-", 0); // prefixed name, adminPlatformThread-0, adminPlatformThread-1, ...
        platformBuilder.uncaughtExceptionHandler(adminThreadExceptionHandler);
        platformBuilder.daemon(true);
        platformBuilder.priority(Thread.MIN_PRIORITY);
        platformBuilder.group(adminPtThreadGroup);     
                
        Thread platformThread0 = platformBuilder.start(voidTask);
        Thread platformThread1 = platformBuilder.start(voidTask);
                
        logger.info("Before starting the platform threads ...");                                                                                 

        platformThread0.join();        
        platformThread1.join();        

        logger.info("After the platform threads has terminated ...");                               
    }
}