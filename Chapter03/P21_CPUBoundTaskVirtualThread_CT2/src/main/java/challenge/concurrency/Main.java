package challenge.concurrency;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final int TASKS_NR = Runtime.getRuntime().availableProcessors();

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

    private static void cpuBoundTask() {         
        logger.info(() -> "1: " + Thread.currentThread().toString());
        logger.info(() -> "1 [#" + Thread.currentThread().threadId() + "] : Generate PI digits ----> ");
        genPIDigits(200000); // for 100000 and virtual threads, some workers can be reused
        logger.info(() -> "1 [#" + Thread.currentThread().threadId() + "] : PI digits generated <----");
    }

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Thread[] vts = new Thread[TASKS_NR];
        long startTime = System.nanoTime();
        for (int i = 0; i < vts.length; i++) {
            int ci = i;           
            vts[i] = Thread.ofPlatform().start(() -> { // switch to ofVirtual() and interpret the results
                cpuBoundTask();
                logger.info(() -> "Task time cpuBoundTask (s): " 
                        + TimeUnit.NANOSECONDS.toSeconds((
                                System.nanoTime() - startTime - TimeUnit.SECONDS.toNanos(ci))));                
            });                        
            Thread.sleep(1000); // give some time between submits
        }      
        
        for (Thread vt : vts) { vt.join(); }
    }
}
