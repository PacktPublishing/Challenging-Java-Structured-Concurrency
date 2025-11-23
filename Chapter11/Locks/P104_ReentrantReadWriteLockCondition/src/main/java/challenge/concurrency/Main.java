package challenge.concurrency;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger
            = Logger.getLogger(Main.class.getName());

    private static final String streamSource = """
                                                And what is music then? Such it is
                                                As are those dulcet sounds in break of day
                                                That creep into the dreaming bridegroom's ear,
                                                And summon him to marriage.
                                                """;
    private static final StringBuilder streamDest = new StringBuilder();

    private static final ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();  
    private static final Condition wcondition = rwlock.writeLock().newCondition();

    private static int cursorWrite;
    private static boolean streamOn = true;

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable readStream = () -> {

            while (streamOn) {

                if (rwlock.isWriteLocked()) { // monitoring point
                    logger.warning(() -> Thread.currentThread().getName()
                            + " reports that the lock is hold by a writer ...");
                }

                rwlock.readLock().lock();  // Acquire read lock                            
               
                try {
                    logger.info(() -> Thread.currentThread() + " read: " + streamDest);
                } finally {
                    rwlock.readLock().unlock();  // Release read lock      
                    try { Thread.sleep(100); } catch (InterruptedException ex) {} // wait for write
                }
            }

            logger.info(() -> Thread.currentThread() + " last read: " + streamDest);
        };

        Runnable writeStream = () -> {

            while (streamOn) {

                rwlock.writeLock().lock();  // Acquire write lock
                try {
                    if (cursorWrite < streamSource.length()) {

                        String str = String.valueOf(streamSource.charAt(cursorWrite++));
                        logger.info(() -> Thread.currentThread() + " is writing ... " + str);
                        streamDest.append(str);
                        
                        try { wcondition.await(100, TimeUnit.MILLISECONDS); } catch (InterruptedException ex) {}
                    } else {
                        streamOn = false;
                    }
                } finally {                    
                    rwlock.writeLock().unlock();  // Release write lock                    
                }
            }
        };

        Thread writer = Thread.ofVirtual().start(writeStream);
        Thread reader1 = Thread.ofVirtual().start(readStream);
        Thread reader2 = Thread.ofVirtual().start(readStream);
        Thread reader3 = Thread.ofVirtual().start(readStream);        

        writer.join();
        reader1.join();
        reader2.join();        
        reader3.join();
    }
}