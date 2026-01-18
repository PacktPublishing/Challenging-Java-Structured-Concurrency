package challenge.concurrency;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        AtomicLong countVirtualThreads = new AtomicLong();
        while (true) {
            Thread.startVirtualThread(() -> {
                long countVirtualThreadsNr
                        = countVirtualThreads.incrementAndGet();
                if (countVirtualThreadsNr % 100_000 == 0) {
                    logger.info(() ->"Virtual threads: " + countVirtualThreadsNr);
                }

                LockSupport.park();
            });
        }

    }
}
