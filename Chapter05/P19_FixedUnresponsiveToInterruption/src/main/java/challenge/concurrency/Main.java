package challenge.concurrency;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    // simulating CPU intensive task  
    private static String genPIDigits(int nrDigits) throws InterruptedException {

        StringBuilder sbpi = new StringBuilder();

        int[] arrDigits = new int[nrDigits + 1];
        int carryOn = 0;

        for (int i = 0; i <= nrDigits; ++i) {
            arrDigits[i] = 2000;
        }

        for (int i = nrDigits; i > 0; i -= 14) {
            if (!Thread.currentThread().isInterrupted()) {
                int sum = 0;
                for (int j = i; j > 0; --j) {
                    if (!Thread.currentThread().isInterrupted()) {
                        sum = sum * j + 10000 * arrDigits[j];
                        arrDigits[j] = sum % (j * 2 - 1);
                        sum /= j * 2 - 1;
                    } else {
                        throw new InterruptedException("Inner computation");
                    }
                }

                sbpi.append(String.format("%04d", carryOn + sum / 10000));
                carryOn = sum % 10000;
            } else {
                throw new InterruptedException("Outer computation");
            }
        }

        return sbpi.toString();
    }

    public static void task() {
        logger.info("Thread started to execute the task");

        try {
            String result = genPIDigits(300000).substring(0, 10);
            logger.info(() -> "Result: " + result + " ...");
        } catch (InterruptedException ex) {
            // Preserve the interrupted status
            Thread.currentThread().interrupt();
            logger.severe(ex.toString());
        }

        logger.info("Thread ended to execute the task");
    }

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Thread thread = Thread.ofVirtual().name("vt").start(() -> task());

        Thread.sleep(2000);  // Main thread sleeps for 2 seconds
        logger.info("Attempt to interrupt the thread ... ... ..."); // The virtual thread will not be interrupted
        thread.interrupt();  // Interrupt the sleeping thread
        thread.join();
    }
}
