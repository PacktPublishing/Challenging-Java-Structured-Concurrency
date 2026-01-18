package challenge.concurrency;

import java.util.logging.Logger;

public class TerminatedVirtualThread {
    
    private static final Logger logger = Logger.getLogger(TerminatedVirtualThread.class.getName());
    
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
    }
    
    public void terminatedVirtualThread() {
        
        Thread vt = Thread.startVirtualThread(() -> {});        

        try {
            vt.join();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            // log "ex"
        }
        
        logger.info(() -> "Terminated Virtual Thread: " + vt.getState());     
    }    
}