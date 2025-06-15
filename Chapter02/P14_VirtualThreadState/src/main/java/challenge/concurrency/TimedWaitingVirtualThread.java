package challenge.concurrency;

import java.util.logging.Logger;

public class TimedWaitingVirtualThread {
    
    private static final Logger logger = Logger.getLogger(TimedWaitingVirtualThread.class.getName());
    
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
    }
    
    public void timedWaitingVirtualThread() {
        
        Thread vt = Thread.startVirtualThread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                // log "ex"
            }
        });      

        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            // log "ex"
        }
     
        logger.info(() -> "Timed Waiting Virtual Thread: " + vt.getState());       
    }    
}