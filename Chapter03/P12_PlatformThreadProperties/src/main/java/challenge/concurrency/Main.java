package challenge.concurrency;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    private static final Thread.UncaughtExceptionHandler adminThreadExceptionHandler 
            = (Thread t, Throwable e) -> {
                logger.severe(() -> "Admin thread exception: " + t.getName());        
            };
     
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        // This is the task (job) executed by our threads
        Runnable voidTask = () -> logger.info(
                () -> "I'm the thread: " + Thread.currentThread().toString());
        
        // creating and starting a platform thread        
        // Thread platformThread = Thread.ofPlatform().start(voidTask);
        Thread platformThread = Thread.ofPlatform().unstarted(voidTask);
        platformThread.setName("adminPlatformThread");
        platformThread.setUncaughtExceptionHandler(adminThreadExceptionHandler);
        platformThread.setDaemon(true); // only for unstarted threads!
        platformThread.setPriority(Thread.MIN_PRIORITY);  
        
        // using name(), uncaughtExceptionHandler(), daemon(), and priority()
        // Thread platformThread = Thread.ofPlatform()
        //        .name("adminPlatformThread")
        //        .uncaughtExceptionHandler(adminThreadExceptionHandler)
        //        .daemon(true)
        //        .priority(Thread.MIN_PRIORITY)
        //        .start(voidTask);
         
        // Thread platformThread = Thread.ofPlatform()
        //        .name("adminPlatformThread")
        //        .uncaughtExceptionHandler(adminThreadExceptionHandler)
        //        .daemon(true)
        //        .priority(Thread.MIN_PRIORITY)
        //        .unstarted(voidTask);
        
        logger.info("Before starting the platform thread ...");                                                                                 
       
        platformThread.start();
        platformThread.join();        

        logger.info("After the platform thread has terminated ...");
    }
}