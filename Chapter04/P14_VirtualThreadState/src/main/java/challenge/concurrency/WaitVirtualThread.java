package challenge.concurrency;

import java.util.logging.Logger;

public class WaitVirtualThread {

    private static final Logger logger = Logger.getLogger(WaitVirtualThread.class.getName());

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
    }

    public void waitingVirtualThread() {

        Thread vt = Thread.startVirtualThread(() -> {
            Thread vt1 = Thread.currentThread();
            Thread vt2 = Thread.ofVirtual().start(() -> {
                try {
                    Thread.sleep(3000);
                    logger.info(() -> "Waiting Virtual Thread: " + vt1.getState());
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    // log "ex"
                }
            });
            
            try { vt2.join(); } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                // log "ex"
            }
        });

        try { vt.join(); } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            // log "ex"
        }
    }
}