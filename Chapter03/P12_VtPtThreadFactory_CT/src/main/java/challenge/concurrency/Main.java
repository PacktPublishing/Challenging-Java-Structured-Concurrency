package challenge.concurrency;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private interface VtPtThreadFactory extends ThreadFactory {

        public Thread newThread(Runnable vt, boolean virtual);
    }

    // custom virtual thread factory that creates unstarted virtual/platform threads
    private static class AdminThreadFactory implements VtPtThreadFactory {

        private static final AtomicInteger threadVtNumber = new AtomicInteger(0);
        private static final String nameVtPrefix = "virtualThread-";
        private static final AtomicInteger threadPtNumber = new AtomicInteger(0);
        private static final String namePtPrefix = "platformThread-";
             
        @Override
        public Thread newThread(Runnable vt, boolean virtual) {
            return virtual ? Thread.ofVirtual().name(nameVtPrefix, threadVtNumber.getAndIncrement()).unstarted(vt) : 
                    Thread.ofPlatform().name(namePtPrefix, threadPtNumber.getAndIncrement()).unstarted(vt);
        }

        @Override
        public Thread newThread(Runnable vt) {
            return newThread(vt, true);
        }
    }

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable voidTask = () -> logger.info(Thread.currentThread().toString());

        AdminThreadFactory adminThreadFactory = new AdminThreadFactory();

        Thread virtualThread1 = adminThreadFactory.newThread(voidTask);        // virtual
        Thread virtualThread4 = adminThreadFactory.newThread(voidTask, false); // platform
        Thread virtualThread2 = adminThreadFactory.newThread(voidTask, true);  // virtual
        Thread virtualThread3 = adminThreadFactory.newThread(voidTask, false); // platform

        virtualThread1.start();
        virtualThread2.start();
        virtualThread3.start();
        virtualThread4.start();

        virtualThread1.join();
        virtualThread2.join();
    }
}
