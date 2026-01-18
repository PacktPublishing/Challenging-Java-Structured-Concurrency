package challenge.concurrency;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class Main {
    
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {
        
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
        
        logger.info("Sleeping 20s ... ");
        Thread.sleep(20000); // connect 'jconsole' while sleeping
        logger.info("Done ... ");

        ScheduledExecutorService executorsch = Executors.newScheduledThreadPool(1);
        executorsch.scheduleAtFixedRate(() -> {
            ThreadMXBean tb = ManagementFactory.getThreadMXBean();
            ThreadInfo[] ti = tb.dumpAllThreads(false, false);
            logger.info(() -> "Platform threads: " + ti.length);
        }, 500, 500, TimeUnit.MILLISECONDS);

        long start = System.nanoTime();
        
        try (ExecutorService executorvt = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 10_000).forEach(i -> {
                executorvt.submit(() -> {
                    Thread.sleep(1000);
                    logger.info(() -> "Task: " + i);
                    return i;
                });
            });
        }
        
        long end = System.nanoTime() - start;
        logger.info(() -> "Time (ms): " + TimeUnit.NANOSECONDS.toMillis(end));
    }
}