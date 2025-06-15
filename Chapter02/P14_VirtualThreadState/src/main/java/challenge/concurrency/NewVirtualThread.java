package challenge.concurrency;

import java.util.logging.Logger;

public class NewVirtualThread {
    
    private static final Logger logger = Logger.getLogger(NewVirtualThread.class.getName());
    
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
    }
    
    public void newVirtualThread() {
        
        Thread vt = Thread.ofVirtual().unstarted(()-> {});               
        logger.info(() -> "New Virtual Thread: " + vt.getState());        
    }    
}