package challenge.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
 
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        CompletableFuture<String>[] arrCf = new CompletableFuture[]{
            CompletableFuture.supplyAsync(() -> {
                try {Thread.sleep((long) (Math.random() * 1000)); } catch (InterruptedException ex) {}
                return "CF1";
            }),
            CompletableFuture.supplyAsync(() -> {
                try {Thread.sleep((long) (Math.random() * 1000)); } catch (InterruptedException ex) {}
                return "CF2";
            }),
            CompletableFuture.supplyAsync(() -> {
                try {Thread.sleep((long) (Math.random() * 1000)); } catch (InterruptedException ex) {}
                return "CF3";
            })
        };

        
        String winner = (String) CompletableFuture.anyOf(arrCf).get();
        
        Stream.of(arrCf)
           .filter(f -> !f.isDone())
                .forEach(f -> {
                    logger.info(() -> "Cancelling " + f); 
                    f.cancel(true);
                });
                        
        logger.info(() -> "Winner: " + winner);        
    }
}