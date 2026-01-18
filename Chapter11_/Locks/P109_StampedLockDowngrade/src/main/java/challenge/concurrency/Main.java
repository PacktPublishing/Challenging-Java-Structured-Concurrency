package challenge.concurrency;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger
            = Logger.getLogger(Main.class.getName());

    private static final Map<Integer, Double> tokens = new HashMap<>();
    private static final StampedLock slock = new StampedLock();

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable readEventuallyWriteTokens = () -> {

            while (true) {

                long stamp = slock.readLock();  // Acquire read lock
                try {
                    int key = (int) (Math.random() * 10);

                    while (tokens.get(key) == null) {

                        // convert to write lock
                        long ws = slock.tryConvertToWriteLock(stamp);

                        if (ws != 0) {

                            logger.info("Successfully converted from read lock to write lock");
                            stamp = ws;

                            double value = Math.random();
                            logger.info(() -> "Fixing key " + key + " (" + value + ")");
                            tokens.put(key, value);
                            
                            break;
                        } else {
                            slock.unlockRead(stamp);  // release the read lock and acquire a write lock in blocking mode
                            stamp = slock.writeLock();
                        }
                    }
                } finally {
                    slock.unlock(stamp);
                    try { Thread.sleep(1000); } catch (InterruptedException ex) {} // wait for write
                }
            }
        };

        Runnable writeTokens = () -> {

            while (true) {

                long stamp = slock.writeLock();  // Acquire write lock
                try {
                    int key = (int) (Math.random() * 10);
                    double value = Math.random();
                    logger.info(() -> Thread.currentThread() + " write key  "
                            + key + " : " + value);
                    tokens.put(key, value);
                } finally {
                    slock.unlock(stamp);  // Release write lock
                    try { Thread.sleep(1000); } catch (InterruptedException ex) {} // wait for read
                }
            }
        };

        Thread writer = Thread.ofVirtual().start(readEventuallyWriteTokens);
        Thread reader = Thread.ofVirtual().start(writeTokens);

        writer.join();
        reader.join();
    }
}
