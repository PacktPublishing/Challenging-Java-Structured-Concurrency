package challenge.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final int NR_THREADS = 10;

    private static final ThreadLocal<String> threadLocal
            = ThreadLocal.<String>withInitial(() -> {
                return "";
            });

    private static final ScopedValue<String> scopedValue = ScopedValue.newInstance();

    public static void runThreadLocal() {
        new Thread(() -> {            
            threadLocal.set("In TH: " + Thread.currentThread().toString());
            logger.info(threadLocal.get());
        }).start();

        logger.info(threadLocal.get());
    }

    public static void runScopedValue() {
        new Thread(() -> {            
            ScopedValue.where(scopedValue, "In SV: " + Thread.currentThread().toString())
                    .run(() -> logger.info(scopedValue.get()));
        }).start();
        logger.info(scopedValue.get());
    }

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        logger.info("ThreadLocal:");
        try (ExecutorService executor
                = Executors.newFixedThreadPool(NR_THREADS)) {
            for (int i = 0; i < NR_THREADS; i++) {
                executor.submit(() -> {
                    threadLocal.set("Out TH: " + Thread.currentThread().toString());
                    runThreadLocal();
                    threadLocal.remove();
                });
            }
        }

        Thread.sleep(1000);
        logger.info("");
        logger.info("ScopedValue:");
        try (ExecutorService executor
                = Executors.newFixedThreadPool(NR_THREADS)) {
            for (int i = 0; i < NR_THREADS; i++) {
                executor.submit(() -> {
                    ScopedValue.where(scopedValue, "Out SV: " + Thread.currentThread().toString())
                            .run(() -> runScopedValue());
                });
            }
        }
    }
}