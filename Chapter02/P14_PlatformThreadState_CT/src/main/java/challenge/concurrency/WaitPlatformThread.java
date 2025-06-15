package challenge.concurrency;

import java.util.logging.Logger;

public class WaitPlatformThread {

    private static final Logger logger = Logger.getLogger(WaitPlatformThread.class.getName());

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
    }

    public void waitingPlatformThread() {

        Thread.ofPlatform().start(()-> {
            Thread pt1 = Thread.currentThread();
            Thread pt2 = Thread.ofPlatform().start(() -> {
                try {
                    Thread.sleep(3000);
                    logger.info(() -> "Waiting Platform Thread: " + pt1.getState());
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    // log "ex"
                }
            });
            
            try { pt2.join(); } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                // log "ex"
            }
        });       
    }
}