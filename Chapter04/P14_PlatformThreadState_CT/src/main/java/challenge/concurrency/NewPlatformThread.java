package challenge.concurrency;

import java.util.logging.Logger;

public class NewPlatformThread {
    
    private static final Logger logger = Logger.getLogger(NewPlatformThread.class.getName());
    
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
    }
    
    public void newPlatformThread() {
        
        Thread pt = Thread.ofPlatform().unstarted(()-> {});         
        logger.info(() -> "New Platform Thread: " + pt.getState());        
    }    
}