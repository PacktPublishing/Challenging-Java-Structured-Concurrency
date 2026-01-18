package challenge.concurrency;

import java.util.logging.Logger;

public class BlockedVirtualThread {
    
    private static final Logger logger = Logger.getLogger(BlockedVirtualThread.class.getName());
    
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
    }
    
    public void blockedVirtualThread() {
        
        Thread vt1 = Thread.ofVirtual().name("vt1").unstarted(new SynchronousClass());
        Thread vt2 = Thread.ofVirtual().name("vt2").unstarted(new SynchronousClass());

        vt1.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            // log "ex"
        }
        vt2.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            // log "ex"
        }

        logger.info(() -> "Thread: " + vt1.getName() + " state: " + vt1.getState());
        logger.info(() -> "Blocked Virtual Thread: " + vt2.getState());

        System.exit(0);        
    }
    
    private static class SynchronousClass implements Runnable {

        @Override
        public void run() {
            logger.info(() -> "Thread in run(): " + Thread.currentThread().getName());
            synchronousCode();
        }

        public static synchronized void synchronousCode() {
            logger.info(() -> "Thread in synchronousCode(): " + Thread.currentThread().getName());
            while (true) {
                // "vt1" will run forever, so "vt2" is blocked
            }
        }
    }
}