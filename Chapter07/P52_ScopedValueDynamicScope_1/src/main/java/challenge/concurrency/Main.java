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
        };               
              
        Thread vt = Thread.ofVirtual().start(() -> {
                 runnableTask.run();
                 ScopedValue.where(SV, "Bumfuzzle").run(runnableTask);                 
                 runnableTask.run();
        });  
        
        Thread.sleep(3000); //give time to 'vt' to complete
                  
        Thread vt123 = Thread.ofVirtual().start(() -> {
                 ScopedValue.where(SV, "Bumfuzzle-1").run(runnableTask);
                 ScopedValue.where(SV, "Bumfuzzle-2").run(runnableTask);
                 ScopedValue.where(SV, "Bumfuzzle-3").run(runnableTask);
        });      
         
        vt.join();
        vt123.join();
    }  
}