package challenge.concurrency;

import java.util.logging.Logger;

public class RunnablePlatformThread {
    
    private static final Logger logger = Logger.getLogger(RunnablePlatformThread.class.getName());
    
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
    }
    
    public void runnablePlatformThread() {
        
        Thread pt = Thread.ofPlatform().unstarted(()-> {
            logger.info(() -> "Runnable Platform Thread: " 
                    + Thread.currentThread().getState());        
        });
        
        pt.start();
        try {
            pt.join();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            // log "ex"
        }
    }    
}