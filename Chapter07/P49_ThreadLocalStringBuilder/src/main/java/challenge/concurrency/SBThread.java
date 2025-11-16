package challenge.concurrency;

import java.util.logging.Logger;

public class SBThread implements Runnable {

    private static final Logger logger = Logger.getLogger(SBThread.class.getName());    
    
    private static final ThreadLocal<StringBuilder> tlv
            = ThreadLocal.<StringBuilder>withInitial(() -> {
                return new StringBuilder("");
            });
    
    @Override
    public void run() {
                
        tlv.get().append(Thread.currentThread());                
        
        logger.info(() -> "Before sleep: " + Thread.currentThread() + " [" + tlv.get() + "]");      
        
        try { Thread.sleep((long) (Math.random() * 3000)); } catch (InterruptedException ex) {}
        
        logger.info(() -> "After sleep: " + Thread.currentThread() + " [" + tlv.get() + "]");      
        
        try { Thread.sleep((long) (Math.random() * 3000)); } catch (InterruptedException ex) {}
        
        tlv.remove();
        
        logger.info(() -> "After remove: " + Thread.currentThread() + " [" + tlv.get() + "]");              
    }                
}