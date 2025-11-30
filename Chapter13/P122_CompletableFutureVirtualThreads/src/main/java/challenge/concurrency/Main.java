package challenge.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        CompletableFuture<String> emailServiceNotification = CompletableFuture.supplyAsync(() -> {
            try {
                logger.info(() -> "Notify service via e-mail by: " + Thread.currentThread().getName());
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            return "Ticket registered: #" + (Math.random() * 10000);
        });

        String ticket = emailServiceNotification.get(); // wait for the ticket, this is blocking
        logger.info(() -> "Ticket: " + ticket + "\n");
        
        CompletableFuture<String> emailServiceNotificationVt1 = CompletableFuture.supplyAsync(() -> {
            try {
                logger.info(() -> "Notify service via e-mail by: " + Thread.currentThread());
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            return "Ticket registered: #" + (Math.random() * 10000);
        }, Executors.newVirtualThreadPerTaskExecutor());

        String ticket1 = emailServiceNotificationVt1.get(); // wait for the ticket, this is blocking
        logger.info(() -> "Ticket: " + ticket1 + "\n");
        
        CompletableFuture<String> emailServiceNotificationVt2 = CompletableFuture.supplyAsync(() -> {
            try {
                logger.info(() -> "Notify service via e-mail by: " + Thread.currentThread());
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            return "Ticket registered: #" + (Math.random() * 10000);
        }, Thread::startVirtualThread);

        String ticket2 = emailServiceNotificationVt2.get(); // wait for the ticket, this is blocking
        logger.info(() -> "Ticket: " + ticket2 + "\n");
    }
}
