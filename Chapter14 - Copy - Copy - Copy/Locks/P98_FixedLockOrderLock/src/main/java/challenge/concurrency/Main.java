package challenge.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final Lock lockAcc1 = new ReentrantLock();
    private static final Lock lockAcc2 = new ReentrantLock();

    public static void transferTo(Account acc1, Account acc2, int value) {

        try {
            if (lockAcc1.tryLock(1, TimeUnit.SECONDS)) { // if we switch lockAcc1 with lockAcc2 then the result is non-deterministic
                try {
                    if (lockAcc2.tryLock(1, TimeUnit.SECONDS)) {
                        try {
                            acc2.withdraw(value);
                            acc1.deposit(value);
                        } finally {
                            lockAcc2.unlock();
                        }
                    }
                } finally {
                    lockAcc1.unlock();
                }
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public static void transferFrom(Account acc1, Account acc2, int value) {

        try {
            if (lockAcc1.tryLock(1, TimeUnit.SECONDS)) { // if we switch lockAcc1 with lockAcc2 then the result is non-deterministic
                try {
                    if (lockAcc2.tryLock(1, TimeUnit.SECONDS)) {
                        try {
                            acc1.withdraw(value);
                            acc2.deposit(value);
                        } finally {
                            lockAcc2.unlock();
                        }
                    }
                } finally {
                    lockAcc1.unlock();
                }
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Account acc1 = new Account(1_000);
        Account acc2 = new Account(1_500);

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            for (int i = 0; i < 1000; i++) {
                executor.submit(() -> transferFrom(acc1, acc2, 50));
                executor.submit(() -> transferTo(acc1, acc2, 50));
            }
        }

        logger.info(() -> "Account 1: " + acc1.getAmount());
        logger.info(() -> "Account 2: " + acc2.getAmount());
    }
}
