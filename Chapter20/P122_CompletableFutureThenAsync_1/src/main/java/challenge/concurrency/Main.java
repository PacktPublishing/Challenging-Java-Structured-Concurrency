package challenge.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");        

        CompletableFuture<String> cf
                = CompletableFuture.supplyAsync(() -> "Hello ")
                        .thenApply(hello -> hello + " dear ")
                        .thenApply(dear -> dear + " George");

        String r1 = cf.get();
        
        CompletableFuture<String> cfa
                = CompletableFuture.supplyAsync(() -> "Hello ")
                        .thenApplyAsync(hello -> hello + " dear ")
                        .thenApplyAsync(dear -> dear + " George");

        String r2 = cfa.get();
        
        logger.info(() -> "R1: " + r1);
        logger.info(() -> "R2: " + r2);
    }
}
