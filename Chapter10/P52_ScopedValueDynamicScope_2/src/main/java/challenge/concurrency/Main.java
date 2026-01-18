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
                     + " | " + (SV.isBound() ? SV.get() : "Not bound"));
            
            Thread.ofVirtual().start(() -> { // spawn another thread
                logger.info(() -> Thread.currentThread().toString() 
                        + " | " +  (SV.isBound() ? SV.get() : "Not bound"));
            });            
            
            // give time to the spawn thread to finish before this thread
            try { Thread.sleep(3000); } catch (InterruptedException ex) {}
            
            logger.info(() -> Thread.currentThread().toString() 
                    + " | " +  (SV.isBound() ? SV.get() : "Not bound"));        
        };               
               
         Thread vt = Thread.ofVirtual().start(() -> {
                 ScopedValue.where(SV, "Bumfuzzle").run(runnableTask);                 
         });              
         
         vt.join();
    }  
}