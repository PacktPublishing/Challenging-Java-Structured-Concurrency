package challenge.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void transferTo(Account acc1, Account acc2, int value) {

        synchronized (acc1) { // if we switch acc1 with acc2 we may cause a deadlock
            synchronized (acc2) {
                acc2.withdraw(value);
                acc1.deposit(value);
            }
        }
    }

    public static void transferFrom(Account acc1, Account acc2, int value) {

        synchronized (acc1) { // if we switch acc1 with acc2 we may cause a deadlock
            synchronized (acc2) {
                acc1.withdraw(value);
                acc2.deposit(value);
            }
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
