package challenge.concurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static class LockedTask implements Runnable {

        private final Lock lock = new ReentrantLock();

        @Override
        public void run() {
            try {
                lock.lock();
                logger.info("Lock acquired ... working under the lock protection ...");

                Thread.sleep(3000); // working under the lock protection
            } catch (InterruptedException e) {
                logger.severe("Thread interrupted while holding the lock. Releasing lock...");
            } finally {
                lock.unlock();  // always release the lock
                logger.info("Lock released ...");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Thread thread = Thread.ofVirtual().name("vt").start(new LockedTask());

        Thread.sleep(1500);  // Thread acquires the lock and start working
        thread.interrupt();  // Interrupt the thread holding the lock

        thread.join();
    }
}
