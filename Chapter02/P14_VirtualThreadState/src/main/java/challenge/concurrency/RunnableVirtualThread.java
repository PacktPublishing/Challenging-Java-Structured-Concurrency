package challenge.concurrency;

import java.util.logging.Logger;

public class RunnableVirtualThread {
    
    private static final Logger logger = Logger.getLogger(RunnableVirtualThread.class.getName());
    
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
    }
    
    public void runnableVirtualThread() {
        
        Thread vt = Thread.startVirtualThread(()-> {
            logger.info(() -> "Runnable Virtual Thread: " 
                    + Thread.currentThread().getState());        
        });
               
        try {
            vt.join();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            // log "ex"
        }
    }    
}