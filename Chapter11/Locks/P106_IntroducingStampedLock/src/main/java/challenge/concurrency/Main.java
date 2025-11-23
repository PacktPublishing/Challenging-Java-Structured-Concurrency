package challenge.concurrency;

import java.util.concurrent.locks.StampedLock;
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

    private static final StampedLock slock = new StampedLock();

    private static int cursorWrite;
    private static boolean streamOn = true;

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable readStream = () -> {

            while (streamOn) {

                if (slock.isWriteLocked()) { // monitoring point
                    logger.warning(() -> Thread.currentThread().getName()
                            + " reports that the lock is hold by a writer ...");
                }

                long stamp = slock.tryOptimisticRead();  // Acquire the optimistic read lock
                
                if (!slock.validate(stamp)) { 
                    stamp = slock.readLock(); 
                    try {
                        logger.info(() -> Thread.currentThread() + " read: " + streamDest);
                    } finally {
                        slock.unlockRead(stamp);   // Release the read-lock      
                        try { Thread.sleep(100); } catch (InterruptedException ex) {} // wait for write                        
                    }
                } else {
                    logger.info(() -> Thread.currentThread() + " optimistic read: " + streamDest);
                }
            }

            logger.info(() -> Thread.currentThread() + " last read: " + streamDest);
        };

        Runnable writeStream = () -> {

            while (streamOn) {

                long stamp = slock.writeLock();  // Acquire the write-lock
                try {
                    if (cursorWrite < streamSource.length()) {

                        String str = String.valueOf(streamSource.charAt(cursorWrite++));
                        logger.info(() -> Thread.currentThread() + " is writing ... " + str);
                        streamDest.append(str);
                    } else {
                        streamOn = false;
                    }
                } finally {                    
                    slock.unlockWrite(stamp);  // Release the write-lock
                    try { Thread.sleep(100); } catch (InterruptedException ex) {} // wait for read
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