package challenge.concurrency;

import java.util.concurrent.CountDownLatch;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        CountDownLatch cdl = new CountDownLatch(1);

        Thread.ofVirtual().start(() -> {
            try {
                new DatabaseServer().startDatabaseServer();
                cdl.countDown();
            } catch (InterruptedException ex) {}
        });

        cdl.await();
    }
}
