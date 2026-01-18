package challenge.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final Lock rLock = new ReentrantLock();
    private static int value;

    public static void main(String[] args) {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable incrementatorTask = () -> {

            rLock.lock();

            try {
                value = value + 1;
                // logger.info(() -> "Current value: " + value
                //   + " | Thread: " + Thread.currentThread());      
            } finally {
                rLock.unlock();
            }
        };

        logger.info(() -> "Incrementing, please wait ...");

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 1_000_000; i++) {
                executor.execute(incrementatorTask);
            }
        }

        logger.info(() -> "Value: " + value);
    }
}
