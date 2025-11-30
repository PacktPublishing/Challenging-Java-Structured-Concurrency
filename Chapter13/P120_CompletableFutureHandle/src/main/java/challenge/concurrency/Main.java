package challenge.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
  
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        interface Signed {}
        record DigitalSign(String document) implements Signed {}
        record RegularSign(String document) implements Signed {}
        
        CompletableFuture<Signed> order = CompletableFuture.supplyAsync(() -> {

            Signed signedOrder = new DigitalSign("Order#1");
            
            if (Math.random() < 0.7d) { throw new IllegalStateException("Digital service unavailable"); }

            return signedOrder;
        }).handle((orderResult, e) -> {
            
            if (e == null) {
                return orderResult;
            }
            
            logger.severe(() -> "Ops! Exception: " + e);
            
            return new RegularSign("Order#1");
        }).thenApply(signedOrder -> {
            
            logger.info(() -> "Registered the signed order ..." + signedOrder);
                        
            return signedOrder;
        });

        Signed result = order.get();
        logger.info(() -> "Result: " + result);
    }
}
