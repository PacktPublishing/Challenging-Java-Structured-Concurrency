package challenge.concurrency;

import java.util.concurrent.SynchronousQueue;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {      

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
        
        SynchronousQueue<Long> sq = new SynchronousQueue<>();

        Runnable insInQueueTask = () -> {
          
            logger.info(() -> Thread.currentThread().toString() 
                    + " attempts to insert (put) in the sync queue");
            
            try {
                sq.put(Long.MAX_VALUE);
                logger.info(() -> Thread.currentThread().toString() + " insert succeed");
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt(); // log "ex"
            }                        
        };     
        
        logger.info("Begin ...");
        Thread vtThread = Thread.startVirtualThread(insInQueueTask);
        
        logger.info(() -> Thread.currentThread().toString() 
                    + " attempts to take from the sync queue");
            
        long maxlong = sq.take();                        
                
        logger.info(() -> Thread.currentThread().toString() 
                + "took from the sync queue: " + maxlong);                                

        vtThread.join();
        logger.info(vtThread.toString());
        
        logger.info("End ...");
    }
}
