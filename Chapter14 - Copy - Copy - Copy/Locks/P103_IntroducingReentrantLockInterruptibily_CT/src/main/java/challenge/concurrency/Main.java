package challenge.concurrency;

import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final ReentrantLock rLock = new ReentrantLock(true);

    private static Thread t;
    private static Thread[] tt;

    private static int index;

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable calibrate = () -> {

            try {

                // switching to lock() will ignore the interruption
                rLock.lockInterruptibly();
            } catch (InterruptedException ex) {
                logger.info(() -> Thread.currentThread() + " was interrupted ...");
                return;
            }
            try {

                if (index == 0) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                    } // give time to all threads to start
                    while (Math.random() < 0.7d) { // calibrate the number of needed threads
                        if (rLock.hasQueuedThread(tt[index])) {
                            tt[index++].interrupt();
                        }
                    }
                } else {
                    logger.info(() -> "Operation executed by ..." + Thread.currentThread());
                }
            } finally {
                rLock.unlock();
            }

        };

        t = new Thread(calibrate);
        t.start();

        Thread.sleep(1000);

        tt = new Thread[10];

        for (int i = 0; i < 10; i++) {
            tt[i] = Thread.ofVirtual().name("vt-" + i).start(calibrate);
        }
        
        for (int i = 0; i < 10; i++) {
            tt[i].join();
        }
    }
}
