package challenge.concurrency;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final Object objLock1 = new Object();
    private static final Object objLock2 = new Object();

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable voidTask1 = () -> {
            synchronized (objLock1) {
                logger.info(() -> "Thread-1: Holding object lock 1 | " + Thread.currentThread());

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }

                genPIDigits(300000); // simulating CPU intensive task 

                logger.info(() -> "Thread-1: Waiting for object lock 2 | " + Thread.currentThread());

                synchronized (objLock2) {
                    logger.info(() -> "Thread-1: Holding object lock 1 and 2 | " + Thread.currentThread());
                }
            }
        };

        Runnable voidTask2 = () -> {
            synchronized (objLock2) {
                logger.info(() -> "Thread-2: Holding object lock 2 | " + Thread.currentThread());

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }

                genPIDigits(300000); // simulating CPU intensive task                   

                logger.info(() -> "Thread-2: Waiting for object lock 1 | " + Thread.currentThread());

                synchronized (objLock1) {
                    logger.info(() -> "Thread-2: Holding object lock 1 and 2 | " + Thread.currentThread());
                }
            }
        };

        // Thread thread1 = Thread.ofPlatform().name("Thread-1").start(voidTask1);
        // Thread thread2 = Thread.ofPlatform().name("Thread-2").start(voidTask2);
        
        Thread thread1 = Thread.ofVirtual().name("Thread-1").start(voidTask1);
        Thread thread2 = Thread.ofVirtual().name("Thread-2").start(voidTask2);

        thread1.join();
        thread2.join();

        logger.info("Done!");
    }

    // simulating CPU intensive task  
    private static String genPIDigits(int nrDigits) {

        StringBuilder sbpi = new StringBuilder();

        int[] arrDigits = new int[nrDigits + 1];
        int carryOn = 0;

        for (int i = 0; i <= nrDigits; ++i) {
            arrDigits[i] = 2000;
        }

        for (int i = nrDigits; i > 0; i -= 14) {
            int sum = 0;
            for (int j = i; j > 0; --j) {
                sum = sum * j + 10000 * arrDigits[j];
                arrDigits[j] = sum % (j * 2 - 1);
                sum /= j * 2 - 1;
            }

            sbpi.append(String.format("%04d", carryOn + sum / 10000));
            carryOn = sum % 10000;
        }
        return sbpi.toString();
    }
}
