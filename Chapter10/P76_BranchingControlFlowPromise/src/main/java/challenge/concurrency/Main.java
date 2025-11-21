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

        CompletableFuture<String> orderPrintPromise = CompletableFuture.supplyAsync(() -> {            
            logger.info(() -> "Preparing order" + " by: " + Thread.currentThread());
            
            return "#2025";
        }, executor)
                .thenApply(order -> {
                    logger.info(() -> "Formatting order "
                                + order + " by: " + Thread.currentThread());
                    return "formatted order " + order;
                })
                .whenComplete((order, exception) -> {                    
                    if (exception == null) {
                        logger.info(() -> "Printing "
                                + order + " by: " + Thread.currentThread());
                    }
                });

        orderPrintPromise.get();
        logger.info(() -> "Job done ? " + orderPrintPromise.isDone());

        executor.shutdown();
    }
}
