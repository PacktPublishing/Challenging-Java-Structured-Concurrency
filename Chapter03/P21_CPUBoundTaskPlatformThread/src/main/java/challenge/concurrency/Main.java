package challenge.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
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
   
    private static void mainIoBoundTask() { // 1
        logger.info(() -> "1: " + Thread.currentThread().toString());
        logger.info(() -> "1 [#" + Thread.currentThread().threadId() + "] : Compute matrix ----> ");  
        genPIDigits(100000);
        logger.info(() -> "1 [#" + Thread.currentThread().threadId() + "] : Matrix computed <----");
    }

    private static void otherIoBoundTask() { // 2
        logger.info(() -> "2: " + Thread.currentThread().toString());
        logger.info(() -> "2 [#" + Thread.currentThread().threadId() + "] : Format input data ----> ");  
        genPIDigits(125000);
        logger.info(() -> "2 [#" + Thread.currentThread().threadId() + "] : Input data format done <----");
    }

    public static void main(String[] args) {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
            
        long startTime = System.nanoTime();
        try (ExecutorService executor = Executors.newFixedThreadPool(1)) {
            executor.submit(() -> {
                mainIoBoundTask();
                logger.info(() -> "Task time mainIoBoundTask (s): "
                        + TimeUnit.NANOSECONDS.toSeconds((System.nanoTime() - startTime)));
            });
            executor.submit(() -> {
                otherIoBoundTask();
                logger.info(() -> "Task time otherIoBoundTask (s): "
                        + TimeUnit.NANOSECONDS.toSeconds((System.nanoTime() - startTime)));
            });
        }
    }
}
