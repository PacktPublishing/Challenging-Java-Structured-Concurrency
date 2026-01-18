package challenge.concurrency;

import java.util.logging.Logger;

public class TerminatedPlatformThread {
    
    private static final Logger logger = Logger.getLogger(TerminatedPlatformThread.class.getName());
    
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
    }
    
    public void terminatedPlatformThread() {
        
        Thread vt = Thread.ofPlatform().start(()-> {});        

        logger.info(() -> "Terminated Platform Thread: " + vt.getState());     
    }    
}