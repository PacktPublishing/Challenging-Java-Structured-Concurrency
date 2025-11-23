package challenge.concurrency;

import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final ReentrantLock rLock = new ReentrantLock(true);

    private static Thread t1;
    private static Thread t2;
    private static boolean twoThreads = true;

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable operation = () -> {

            while (twoThreads) {
                try {
                    // switching to lock() will ignore the interruption
                    rLock.lockInterruptibly();
                } catch (InterruptedException ex) {

                    logger.warning(() -> Thread.currentThread().toString() + " was interrupted ...");

                    // the other thread continue to run for a while                   
                    try {Thread.sleep(10000); } catch (InterruptedException ex1) {}

                    logger.warning("Stopping the operation ...");
                    twoThreads = false; // stop the operation

                    return;
                }
                
                try {

                    // simulate the alternative operation and interrupting one of the threads
                    logger.info(() -> "Operation executed by " + Thread.currentThread());
                    
                    try { Thread.sleep(2000); } catch (InterruptedException ex) {}
                    
                    if (Math.random() < 0.2d) {
                        if (rLock.hasQueuedThread(t1)) {
                            t1.interrupt();
                        } else if (rLock.hasQueuedThread(t2)) {
                            t2.interrupt();
                        }
                    }
                } finally {
                    rLock.unlock();
                }
            }
        };

        t1 = Thread.ofVirtual().name("t1").start(operation);
        t2 = Thread.ofVirtual().name("t2").start(operation);

        t1.join();
        t2.join();
    }
}
