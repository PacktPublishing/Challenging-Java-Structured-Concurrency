package challenge.concurrency;

import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;

public class Main {
                  
    private static final Logger logger = Logger.getLogger(Main.class.getName());
      
    // custom virtual thread factory that creates unstarted virtual threads
    private static class AdminThreadFactory implements ThreadFactory {
              
        @Override
        public Thread newThread(Runnable vt) {            
            return Thread.ofVirtual()
                    .unstarted(vt); // unstarted virtual thread
        }
    }    

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable voidTask = () -> logger.info(Thread.currentThread().toString());               
        
        // a thread factory to create unstarted threads from OfVirtual builder
        ThreadFactory vtThreadFactory = Thread.ofVirtual().factory();
        
        // Thread.Builder builder = Thread.ofVirtual();
        // ThreadFactory vtThreadFactory = builder.factory();        
        
        AdminThreadFactory adminThreadFactory = new AdminThreadFactory();       
        
        Thread virtualThread1 = vtThreadFactory.newThread(voidTask); 
        Thread virtualThread2 = adminThreadFactory.newThread(voidTask);
        
        virtualThread1.start();
        virtualThread2.start();
        
        virtualThread1.join();
        virtualThread2.join();        
    }
}