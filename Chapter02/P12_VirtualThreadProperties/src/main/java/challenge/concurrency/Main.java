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
        
        // creating and starting a virtual thread     
        // Thread virtualThread = Thread.startVirtualThread(voidTask);
        // Thread virtualThread = Thread.ofVirtual().start(voidTask);
        Thread virtualThread = Thread.ofVirtual().unstarted(voidTask);
        virtualThread.setName("adminVirtualThread");
        virtualThread.setUncaughtExceptionHandler(adminThreadExceptionHandler);
                        
        // DON'T DO THIS, A VIRTUAL THREAD IS ALWAYS A DAEMON THREAD
        // virtualThread.setDaemon(false); 
        
        // DON'T DO THIS, A VIRTUAL THREAD PRIORITY IS ALWAYS NORM_PRIORITY
        // virtualThread.setPriority(Thread.MIN/MAX_PRIORITY); 
                
        // using name() and uncaughtExceptionHandler()
        // Thread virtualThread = Thread.ofVirtual()
        //        .name("adminVirtualThread")
        //        .uncaughtExceptionHandler(adminThreadExceptionHandler).start(voidTask);
         
        // Thread virtualThread = Thread.ofVirtual()
        //        .name("adminVirtualThread")
        //        .uncaughtExceptionHandler(adminThreadExceptionHandler).unstarted(voidTask);
         
        logger.info("Before starting the virtual thread ...");                                                                                 
       
        virtualThread.start();
        virtualThread.join();        

        logger.info("After the virtual thread has terminated ...");
    }
}