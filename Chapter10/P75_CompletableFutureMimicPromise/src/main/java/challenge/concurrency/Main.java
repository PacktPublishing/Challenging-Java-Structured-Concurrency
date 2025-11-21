package challenge.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        CompletableFuture<Void> orderPrintPromise = CompletableFuture.supplyAsync(() -> {
            try {
                logger.info(() -> "Order fetched by: " + Thread.currentThread());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            return "#2025";
        }, executor)
                .thenAccept(order -> logger.info(() -> "Print order " 
                        + order + " by: " + Thread.currentThread()))
                .exceptionally(e -> { logger.severe(() -> "Exception: " 
                        + e + " Thread: " + Thread.currentThread());
                    return null;
                });
        
        orderPrintPromise.get();
        logger.info(() -> "Job done ? " + orderPrintPromise.isDone());
        
        executor.shutdown();
    }
}