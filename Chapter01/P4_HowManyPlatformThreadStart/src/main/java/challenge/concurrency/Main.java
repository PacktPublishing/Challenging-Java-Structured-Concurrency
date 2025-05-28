package challenge.concurrency;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        AtomicLong countPlatformThreads = new AtomicLong();
        
        while (true) {
            
            Thread.ofPlatform().start(() -> { // new Thread(() -> {
                long countPlatformThreadsNr
                        = countPlatformThreads.incrementAndGet();
                if (countPlatformThreadsNr % 1_000 == 0) {
                    logger.info(() ->"Platform threads: " + countPlatformThreadsNr);
                }

                LockSupport.park();
            });
        }
    }
}
