package challenge.concurrency;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());       
    
    private static final List<Integer> listInts = List.of(5, 2, 9, 11, 12, 1, 3, 55, 3);
      
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
    
        CompletableFuture<Integer> minCf = CompletableFuture.supplyAsync(() -> Collections.min(listInts));
        CompletableFuture<Integer> maxCf = CompletableFuture.supplyAsync(() -> Collections.max(listInts));
        
        CompletableFuture<Integer> avgCf = minCf.thenCombine(maxCf, (min, max) -> ((min + max) / 2));
        
        int avg = avgCf.get();
        logger.info(() -> "Min-max avg: " + avg);
    }
}
