package challenge.concurrency;

import java.util.concurrent.SynchronousQueue;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        SynchronousQueue<Long> sq = new SynchronousQueue<>();
        Thread[] threads = new Thread[2]; // holds the threads to be referred later

        Runnable insInQueueTask = () -> {

            while (!Thread.currentThread().isInterrupted()) {
                logger.info(() -> Thread.currentThread().toString()
                        + " attempts to insert (put) in the sync queue");

                // interruption randomly caused
                if (Math.random() < 0.2d) {
                    logger.info(() -> Thread.currentThread().getName() + " caused an interruption");
                    Thread.currentThread().interrupt(); 
                }

                try {
                    sq.put(Long.MAX_VALUE);
                    logger.info(() -> Thread.currentThread().toString() + " insert succeed");
                } catch (InterruptedException ex) {                    
                    Thread.currentThread().interrupt(); // log "ex"                    
                }
            }

            logger.severe(() -> "InterruptedException! The thread "
                            + Thread.currentThread().getName() + " was interrupted!");
            
            threads[0].interrupt(); // vtTakeThread
        };

        Runnable takeFromQueueTask = () -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    logger.info(() -> Thread.currentThread().toString()
                            + " attempts to take from the sync queue");

                    // interruption randomly caused
                    if (Math.random() < 0.2d) {
                        logger.info(() -> Thread.currentThread().getName() + " caused an interruption");
                        Thread.currentThread().interrupt();
                    }

                    long maxlong = sq.take();

                    logger.info(() -> Thread.currentThread().toString()
                            + " took from the sync queue: " + maxlong);
                } catch (InterruptedException ex) {                    
                    Thread.currentThread().interrupt(); // log "ex"                    
                }
            }

            logger.severe(() -> "InterruptedException! The thread "
                            + Thread.currentThread().getName() + " was interrupted!");
            
            threads[1].interrupt(); // vtInsThread
        };

        logger.info("Begin ...");

        Thread vtTakeThread = Thread.ofVirtual()
                .name("vtTakeThread").start(takeFromQueueTask);
        Thread vtInsThread = Thread.ofVirtual()
                .name("vtInsThread").start(insInQueueTask);
        
        threads[0] = vtTakeThread;
        threads[1] = vtInsThread;
        
        vtInsThread.join();
        vtTakeThread.join();

        logger.info(vtInsThread.toString());
        logger.info(vtTakeThread.toString());

        logger.info("End ...");
    }
}
