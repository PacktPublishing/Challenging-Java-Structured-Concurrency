package challenge.concurrency;

import java.lang.ScopedValue.Carrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final int NR_THREADS = 10;

    private static final InheritableThreadLocal<String> 
            threadLocal = new InheritableThreadLocal<>();

    private static final ScopedValue<String> scopedValue = ScopedValue.newInstance();

    public static void runThreadLocal() {
        new Thread(() -> {
            logger.info(() -> "In TH-" + threadLocal.get());            
        }).start();

        logger.info(threadLocal.get());
    }

    public static void runScopedValue(Carrier carrier) {
        new Thread(() -> {
            logger.info(() -> "In SV-" + carrier.get(scopedValue));            
        }).start();
        logger.info(carrier.get(scopedValue));
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
                    Carrier carrier = ScopedValue.where(
                            scopedValue, "Out SV: " + Thread.currentThread().toString());
                    carrier.run(() -> runScopedValue(carrier));
                });
            }
        }
    }
}