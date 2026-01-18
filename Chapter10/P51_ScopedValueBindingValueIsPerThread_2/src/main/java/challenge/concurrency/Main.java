package challenge.concurrency;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    private static final ScopedValue<String> SV = ScopedValue.newInstance();

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");        
        
        Runnable runnableTask = () -> {
            logger.info(() -> Thread.currentThread().toString() 
                    + " | before pause | " + (SV.isBound() ? SV.get() : "Not bound"));
            
            try { Thread.sleep((long) (Math.random() * 5000)); } catch (InterruptedException ex) {}
            
            logger.info(() -> Thread.currentThread().toString() 
                    + " | after pause | " + (SV.isBound() ? SV.get() : "Not bound"));        
        };
        
        // platform threads
         Thread pt1 = new Thread(() ->
                 ScopedValue.where(SV, "Bumfuzzle-#" 
                         + Thread.currentThread().threadId()).run(runnableTask));
         pt1.start();
         
         Thread pt2 = Thread.ofPlatform().start(() ->
                 ScopedValue.where(SV, "Bumfuzzle-#" 
                         + Thread.currentThread().threadId()).run(runnableTask));
         
         Thread pt3 = Thread.ofPlatform().unstarted(() ->
                 ScopedValue.where(SV, "Bumfuzzle-#" 
                         + Thread.currentThread().threadId()).run(runnableTask));
         pt3.start();
         
         // virtual threads
         Thread vt1 = Thread.ofVirtual().start(() ->
                 ScopedValue.where(SV, "Bumfuzzle-#" 
                         + Thread.currentThread().threadId()).run(runnableTask));
         Thread vt2 = Thread.ofVirtual().unstarted(() ->
                 ScopedValue.where(SV, "Bumfuzzle-#" 
                         + Thread.currentThread().threadId()).run(runnableTask));
         vt2.start();
         
         vt1.join();
         vt2.join();
    }  
}