package challenge.concurrency;

import java.io.IOException;
import java.lang.ScopedValue.Carrier;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    private static final ScopedValue<String> SV = ScopedValue.newInstance();

    public static void main(String[] args) throws Exception {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");        
        
        Runnable runnableTask = () -> {
            logger.info(() -> Thread.currentThread().toString() 
                    + " | " + (SV.isBound() ? SV.get() : "Not bound"));
        };
        
        Callable<Boolean> callableTask = () -> {
            logger.info(() -> Thread.currentThread().toString()
                    + " | " + (SV.isBound() ? SV.get() : "Not bound"));
            
            return true;
        };
        
        Callable<Boolean> callableTaskExc = new Callable<>() {
            @Override
            public Boolean call() throws IOException {
                logger.info(() -> Thread.currentThread().toString()
                        + " | " + (SV.isBound() ? SV.get() : "Not bound"));
                
                if(SV.isBound() && !SV.get().equals("bumfuzzle.txt")) {
                    throw new IOException("The file name doesn't match");
                }
                
                return true;
            }
        };
                      
        logger.info("ScopedValue/Runnable");
        logger.info("--------------------");        
        
        runnableTask.run();                                    // Not bound                
        
        Carrier cr = ScopedValue.where(SV, "Bumfuzzle!");
        cr.run(runnableTask);                                  // Bumfuzzle!
        
        ScopedValue.where(SV, "Bumfuzzle!").run(runnableTask); // Bumfuzzle!       
        
        runnableTask.run();                                    // Not bound
        
        logger.info("");        
        logger.info("ScopedValue/CallableOp");
        logger.info("----------------------");        
        
        callableTask.call();                                                       // Not bound
        
        ScopedValue.CallableOp<Boolean, Exception> ct = () -> callableTask.call();                 
        ct.call();                                                                 // Not bound
        ScopedValue.where(SV, "Bumfuzzle!").call(ct);                              // Bumfuzzle!
        
        ScopedValue.where(SV, "Bumfuzzle!").call(() -> callableTask.call());       // Bumfuzzle!        
        
        Carrier cc = ScopedValue.where(SV, "Bumfuzzle!");                     
        cc.call(() -> callableTask.call());                                        // Bumfuzzle!  
        
        callableTask.call();                                                       // Not bound  
        
        ScopedValue.CallableOp<Boolean, IOException> cte = () -> {
            try {
                return callableTaskExc.call();
            } catch (Exception ex) {
                logger.severe(ex.toString());
            }
            return false;
        }; 
        cte.call();                                                                // Not bound
        ScopedValue.where(SV, "bumfuzzle.txt").call(cte);                          // bumfuzzle.txt
        ScopedValue.where(SV, "Bumfuzzle!").call(cte);                             // Bumfuzzle! / java.io.IOException: The file name doesn't match                
    }  
}