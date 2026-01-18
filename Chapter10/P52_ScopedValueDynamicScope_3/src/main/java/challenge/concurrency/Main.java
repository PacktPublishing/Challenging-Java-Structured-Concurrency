package challenge.concurrency;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    private static final ScopedValue<String> SV = ScopedValue.newInstance();

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");        
        
        Runnable runnableTask3 = () -> {
            logger.info(() -> "RT3: " + Thread.currentThread().toString() 
                    + " | " + (SV.isBound() ? SV.get() : "Not bound"));                          
        };
        
        Runnable runnableTask2 = () -> {
            logger.info(() -> "RT2: " + Thread.currentThread().toString() 
                    + " | " + (SV.isBound() ? SV.get() : "Not bound"));                          
            
            runnableTask3.run();
            
            logger.info(() -> "RT2: " + Thread.currentThread().toString() 
                    + " | " + (SV.isBound() ? SV.get() : "Not bound"));                          
        };
        
        Runnable runnableTask1 = () -> {
            logger.info(() -> "RT1: " + Thread.currentThread().toString() 
                    + " | " + (SV.isBound() ? SV.get() : "Not bound"));                          
            
            runnableTask2.run();
            
            logger.info(() -> "RT1: " + Thread.currentThread().toString() 
                    + " | " + (SV.isBound() ? SV.get() : "Not bound"));                          
        };               
                              
        Thread vt = Thread.ofVirtual().start(() -> {                 
                 ScopedValue.where(SV, "Bumfuzzle").run(runnableTask1);                                  
        });                 
         
        vt.join();        
    }  
}