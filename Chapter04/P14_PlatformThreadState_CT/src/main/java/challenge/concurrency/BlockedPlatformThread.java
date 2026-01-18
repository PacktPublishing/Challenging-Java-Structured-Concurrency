package challenge.concurrency;

import java.util.logging.Logger;

public class BlockedPlatformThread {
    
    private static final Logger logger = Logger.getLogger(BlockedPlatformThread.class.getName());
    
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
    }
    
    public void blockedPlatformThread() {
        
        Thread pt1 = Thread.ofPlatform().name("pt1").unstarted(new SynchronousClass());
        Thread pt2 = Thread.ofPlatform().name("pt2").unstarted(new SynchronousClass());

        pt1.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            // log "ex"
        }
        pt2.start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            // log "ex"
        }

        logger.info(() -> "Thread: " + pt1.getName() + " state: " + pt1.getState());
        logger.info(() -> "Blocked Virtual Thread: " + pt2.getState());

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