package challenge.concurrency;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;

public class Main {
    
    private static final Logger logger = Logger.getLogger(Main.class.getName());
   
    // custom platform thread factory that creates unstarted platform threads
    private static class AdminThreadFactory implements ThreadFactory {
              
        @Override
        public Thread newThread(Runnable vt) {
            
            // return new Thread(vt); // without using ofPlatform(), old-style           
            
            return Thread.ofPlatform()                    
                    .unstarted(vt);            
        }
    }        

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable voidTask = () -> logger.info(Thread.currentThread().toString());  
        
        ThreadFactory oldStyleThreadFactory = Executors.defaultThreadFactory();
        
        // a thread factory to create unstarted threads from OfPlatform builder
        ThreadFactory ptThreadFactory = Thread.ofPlatform().factory();
        
        // Thread.Builder builder = Thread.ofPlatform();
        // ThreadFactory ptThreadFactory = builder.factory(); 
        
        AdminThreadFactory adminThreadFactory = new AdminThreadFactory();
        
        Thread platformThread1 = ptThreadFactory.newThread(voidTask); 
        Thread platformThread2 = adminThreadFactory.newThread(voidTask);
        Thread platformThread3 = oldStyleThreadFactory.newThread(voidTask);
        
        platformThread1.start();
        platformThread2.start();  
        platformThread3.start();
    }
}