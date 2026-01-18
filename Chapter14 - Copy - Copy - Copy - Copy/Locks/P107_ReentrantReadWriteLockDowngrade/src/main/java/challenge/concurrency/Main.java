package challenge.concurrency;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger
            = Logger.getLogger(Main.class.getName());

    private static final Map<Integer, Double> tokens = new HashMap<>();
    private static final ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable readEventuallyWriteTokens = () -> {

            while (true) {

                rwlock.readLock().lock();  // Acquire read lock
                try {
                    int key = (int) (Math.random() * 10);

                    if (tokens.get(key) == null) {

                        // Must release read lock before acquiring write lock
                        rwlock.readLock().unlock();
                        rwlock.writeLock().lock();

                        try {
                            // Recheck state because another thread might have
                            // acquired write lock and changed state before we did.
                            if (tokens.get(key) == null) {

                                double value = Math.random();
                                logger.info(() -> "Fixing key " + key + " (" + value + ")");
                                tokens.put(key, value);
                            }
                            // Downgrade by acquiring read lock before releasing write lock
                            rwlock.readLock().lock();
                        } finally {
                            rwlock.writeLock().unlock(); // Unlock write, still hold read
                        }
                    }

                    logger.info(() -> Thread.currentThread() + " read key "
                            + key + ": " + tokens.get(key));
                } finally {
                    rwlock.readLock().unlock();  // Release read lock      
                    try { Thread.sleep(1000); } catch (InterruptedException ex) {} // wait for write
                }
            }
        };

        Runnable writeTokens = () -> {

            while (true) {

                rwlock.writeLock().lock();  // Acquire write lock
                try {
                    int key = (int) (Math.random() * 10);
                    double value = Math.random();
                    logger.info(() -> Thread.currentThread() + " write key "
                            + key + ": " + value);
                    tokens.put(key, value);
                } finally {
                    rwlock.writeLock().unlock();  // Release write lock
                    try { Thread.sleep(1000); } catch (InterruptedException ex) {} // wait for read
                }
            }
        };

        Thread writer = Thread.ofVirtual().name("writer-thread").start(writeTokens);
        Thread reader = Thread.ofVirtual().name("reader-thread").start(readEventuallyWriteTokens);

        writer.join();
        reader.join();
    }
}
