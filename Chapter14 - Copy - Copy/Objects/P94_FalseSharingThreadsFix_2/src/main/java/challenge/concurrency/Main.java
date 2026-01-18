package challenge.concurrency;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final long ITERATIONS_NR = 1_000_000_000;

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        VolatileCounter vc1 = new VolatileCounter();
        VolatileCounter vc2 =  vc1;

        Thread t1 = Thread.ofVirtual().unstarted(() -> {
            long st = System.nanoTime();
            for (long i = 0; i < ITERATIONS_NR; i++) {
                vc1.x++;
            }
            long et = System.nanoTime();

            logger.info(() -> "Time (vc1): " + (et - st) + " ns ("
                    + TimeUnit.NANOSECONDS.toSeconds(et - st) + " s)");
        });

        Thread t2 = Thread.ofVirtual().unstarted(() -> {
            long st = System.nanoTime();
            for (long i = 0; i < ITERATIONS_NR; i++) {
                vc2.y++;
            }
            long et = System.nanoTime();

            logger.info(() -> "Time (vc2): " + (et - st) + " ns ("
                    + TimeUnit.NANOSECONDS.toSeconds(et - st) + " s)");
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();        
    }
}
