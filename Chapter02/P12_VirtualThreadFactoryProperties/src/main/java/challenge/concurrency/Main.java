package challenge.concurrency;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Main {
                  
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    private static final Thread.UncaughtExceptionHandler adminThreadExceptionHandler 
            = (Thread t, Throwable e) -> {
                logger.severe(() -> "Thread exception: " + t.getName());        
            };
      
    // custom virtual thread factory that creates unstarted virtual threads
    private static class AdminThreadFactory implements ThreadFactory {
              
        private static final Thread.UncaughtExceptionHandler adminThreadExceptionHandler 
            = (Thread t, Throwable e) -> {
                logger.severe(() -> "Admin thread exception: " + t.getName());        
            };
        
        private static final AtomicInteger threadNumber = new AtomicInteger(0);
        private static final String namePrefix = "adminVirtualThread";
        
        @Override
        public Thread newThread(Runnable vt) {            
            return Thread.ofVirtual()
                    .name(namePrefix + "-" + threadNumber.getAndIncrement())
                    .uncaughtExceptionHandler(adminThreadExceptionHandler)
                    .unstarted(vt); // unstarted virtual thread
        }
    }    

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable voidTask = () -> logger.info(
                () -> "I'm the thread: " + Thread.currentThread().toString());
        
        // a thread factory to create unstarted threads from OfVirtual builder
        ThreadFactory adminThreadFactory0 = Thread.ofVirtual()
                .name("adminVirtualThread-", 0)
                .uncaughtExceptionHandler(adminThreadExceptionHandler)
                .factory();              
        
        // Thread.Builder builder = Thread.ofVirtual();
        // ThreadFactory adminThreadFactory0 = builder.factory();        
        
        AdminThreadFactory adminThreadFactory1 = new AdminThreadFactory();       
        
        Thread virtualThread0 = adminThreadFactory0.newThread(voidTask); 
        Thread virtualThread1 = adminThreadFactory1.newThread(voidTask);
        
        virtualThread0.start();
        virtualThread1.start();
        
        virtualThread0.join();
        virtualThread1.join();        
    }
}