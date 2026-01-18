package challenge.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
            return "Async operation executed by " + Thread.currentThread();
        });        
                
        String cfResult = cf.get();
        logger.info(cfResult);
    }
}
