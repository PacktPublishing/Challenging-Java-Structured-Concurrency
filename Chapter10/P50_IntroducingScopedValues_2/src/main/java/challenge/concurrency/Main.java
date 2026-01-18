package challenge.concurrency;

import java.io.IOException;
import java.lang.ScopedValue.Carrier;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    private static final ScopedValue<String> SV = ScopedValue.newInstance();
    
    public static void taskNoReturn() {
        logger.info(() -> Thread.currentThread().toString() 
                    + " | " + (SV.isBound() ? SV.get() : "Not bound"));
    }
    
    public static boolean taskWithReturn() {
        logger.info(() -> Thread.currentThread().toString()
                    + " | " + (SV.isBound() ? SV.get() : "Not bound"));
            
        return true;
    }
    
    public static boolean taskWithReturnAndExc() throws IOException {
        
        logger.info(() -> Thread.currentThread().toString()
                    + " | " + (SV.isBound() ? SV.get() : "Not bound"));
            
        if (SV.isBound() && !SV.get().equals("bumfuzzle.txt")) {
            throw new IOException("The file name doesn't match");
        }
            
        return true;
    }

    public static void main(String[] args) throws Exception {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");                       
                      
        logger.info("ScopedValue/Runnable");
        logger.info("--------------------");        
                
        taskNoReturn();                                                         // Not bound
        
        ScopedValue.where(SV, "Bumfuzzle!").run(() -> taskNoReturn());          // Bumfuzzle!       
        
        Carrier cr = ScopedValue.where(SV, "Bumfuzzle!");
        cr.run(() -> taskNoReturn());                                           // Bumfuzzle!
        
        taskNoReturn();                                                         // Not bound        
        
        logger.info("");        
        logger.info("ScopedValue/CallableOp");
        logger.info("----------------------");        
        
        taskWithReturn();                                                       // Not bound
        
        ScopedValue.CallableOp<Boolean, Exception> twr = () -> taskWithReturn(); 
        twr.call();                                                             // Not bound
        ScopedValue.where(SV, "Bumfuzzle!").call(twr);                          // Bumfuzzle!
        
        ScopedValue.where(SV, "Bumfuzzle!").call(() -> taskWithReturn());       // Bumfuzzle!        
        
        Carrier cc = ScopedValue.where(SV, "Bumfuzzle!");                     
        cc.call(() -> taskWithReturn());                                        // Bumfuzzle! 
        
        taskWithReturn();                                                       // Not bound
        
        ScopedValue.CallableOp<Boolean, IOException> twre = () -> {
            try {
                return taskWithReturnAndExc();
            } catch (IOException ex) {
                logger.severe(ex.toString());
            }
            return false;
        }; 
        twre.call();                                                            // Not bound
        ScopedValue.where(SV, "bumfuzzle.txt").call(twre);                      // bumfuzzle.txt
        ScopedValue.where(SV, "Bumfuzzle!").call(twre);                         // Bumfuzzle! / java.io.IOException: The file name doesn't match                
    }  
}