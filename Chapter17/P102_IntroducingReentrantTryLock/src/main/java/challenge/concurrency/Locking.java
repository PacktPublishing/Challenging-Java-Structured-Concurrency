package challenge.concurrency;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class Locking {

    private static final Logger logger
            = Logger.getLogger(Locking.class.getName());

    private static final ReentrantLock lock = new ReentrantLock();

    public boolean execute() throws InterruptedException {

        if (!lock.tryLock(3, TimeUnit.SECONDS)) {
            logger.info(() -> "Acquiring the lock attempt failed via "
                    + Thread.currentThread().getName());
            return false;
        } else {
            logger.info(() -> "The lock was successfully acquired by "
                    + Thread.currentThread().getName());
        }

        try {
            logger.info(() -> "Thread " + Thread.currentThread().getName()
                    + " started its execution ...");
            Thread.sleep(Duration.ofSeconds(10));
            logger.info(() -> "Thread " + Thread.currentThread().getName()
                    + " finished its execution ...");
        } finally {
            lock.unlock();
        }

        return true;
    }

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Locking locking = new Locking();

        Runnable task = () -> {
            try {
                // we try 5 times to acquire the lock
                for (int i = 0; i < 5; i++) {
                    locking.execute();
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                logger.severe(() -> "Exception: " + ex);
            }
        };

        Thread t1 = Thread.ofVirtual().name("t1").start(task);        

        // give time to thread 't1' to acquire the lock
        Thread.sleep(1000);

        Thread t2 = Thread.ofVirtual().name("t2").start(task);                
        
        t1.join();
        t2.join();
    }
}
