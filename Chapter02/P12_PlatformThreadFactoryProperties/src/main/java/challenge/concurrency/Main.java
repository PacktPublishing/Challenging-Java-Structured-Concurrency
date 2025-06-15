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
    
    private static final ThreadGroup adminPtThreadGroup = new ThreadGroup("PlatformThreads");
   
    // custom platform thread factory that creates unstarted platform threads
    private static class AdminThreadFactory implements ThreadFactory {
              
        private static final Thread.UncaughtExceptionHandler adminThreadExceptionHandler 
            = (Thread t, Throwable e) -> {
                logger.severe(() -> "Admin thread exception: " + t.getName());        
            };
    
        private static final ThreadGroup adminPtThreadGroup = new ThreadGroup("AdminPlatformThreads");
        
        private static final AtomicInteger threadNumber = new AtomicInteger(0);
        private static final String namePrefix = "adminPtThread";
    
        @Override
        public Thread newThread(Runnable vt) {
                        
            return Thread.ofPlatform()   
                    .name(namePrefix + "-" + threadNumber.getAndIncrement())
                    .uncaughtExceptionHandler(adminThreadExceptionHandler)
                    .daemon(true)
                    .priority(Thread.MIN_PRIORITY)
                    .group(adminPtThreadGroup)
                    .unstarted(vt);            
        }
    }        

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable voidTask = () -> logger.info(
                () -> "I'm a thread: " + Thread.currentThread().toString());
                 
        // a thread factory to create unstarted threads from OfPlatform builder
        ThreadFactory adminThreadFactory0 = Thread.ofPlatform()
                .name("adminPlatformThread-", 0)
                .uncaughtExceptionHandler(adminThreadExceptionHandler)
                .daemon(true)
                .priority(Thread.MIN_PRIORITY)
                .group(adminPtThreadGroup)
                .factory();
        
        // Thread.Builder builder = Thread.ofPlatform();
        // ThreadFactory adminThreadFactory0 = builder.factory(); 
        
        AdminThreadFactory adminThreadFactory1 = new AdminThreadFactory();
        
        Thread platformThread0 = adminThreadFactory0.newThread(voidTask); 
        Thread platformThread1 = adminThreadFactory1.newThread(voidTask);
        
        platformThread0.start();
        platformThread1.start();          
        
        platformThread0.join();
        platformThread1.join();  
    }
}