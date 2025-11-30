package challenge.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        logger.info(() -> "Main thread: " + Thread.currentThread());
        
        CompletableFuture<String> cf
                = CompletableFuture.supplyAsync(() -> {
                    
                    logger.info(() -> "Thread CF: " + Thread.currentThread());
                    
                    try { Thread.sleep(3000); } catch (InterruptedException ex) {}
                    
                    return "And the winner is: ";
                });              

        CompletableFuture<String> cf1 = cf.thenApplyAsync( 
                t -> {
                    logger.info(() -> "Thread CF-1: " + Thread.currentThread());
                    try { Thread.sleep((long) (Math.random() * 1000)); } catch (InterruptedException ex) {}                   
                    return t + " Mike";
                });

        CompletableFuture<String> cf2 = cf.thenApplyAsync( 
                t -> {
                    logger.info(() -> "Thread CF-2: " + Thread.currentThread());
                    try { Thread.sleep((long) (Math.random() * 1000)); } catch (InterruptedException ex) {}
                    return t + " Kelly";
                });

        while (!cf1.isDone() && !cf2.isDone()) {}

        if (cf1.isDone()) {
            logger.info(() -> "CF-1: " + cf1.resultNow());
        }
        
        if (cf2.isDone()) {
            logger.info(() -> "CF-2: " + cf2.resultNow());
        }
    }
}
