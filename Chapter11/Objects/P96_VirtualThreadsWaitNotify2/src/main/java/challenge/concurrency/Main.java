package challenge.concurrency;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Object o = new Object();

        Thread vthread1 = Thread.ofVirtual().name("vt-1").unstarted(() -> {
            synchronized (o) {
                try {
                    logger.info("Before invoking the wait() method ...");
                    logger.info(() -> Thread.currentThread().getName() 
                            + " | " + Thread.currentThread().getState());
                    o.wait();
                    logger.info("After invoking the notify() method ...");
                    logger.info(() -> Thread.currentThread().getName()
                            + " | " + Thread.currentThread().getState());
                } catch (InterruptedException e) {}
            }
        });
        
        Thread vthread2 = Thread.ofVirtual().name("vt-2").unstarted(() -> {
            synchronized (o) {
                logger.info(() -> Thread.currentThread().getName() 
                        + " invokes the notify() method ...");
                o.notify();
            }
        });                
        
        vthread2.start();        
        Thread.sleep(500); // starting 'vt-2'
        
        vthread1.start();
        Thread.sleep(500); // starting 'vt-1'
        logger.info("After invoking the wait() method ...");  
        logger.info(() -> vthread1.getName() + " | " + vthread1.getState());               
        
        vthread1.join(); // notify() call was missed, so 'vt-1' will wait forever
    }
}