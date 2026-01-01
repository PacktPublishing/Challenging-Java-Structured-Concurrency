package challenge.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        logger.info(() -> "Main thread: " + Thread.currentThread());

        CompletableFuture<String> cf
                = CompletableFuture.supplyAsync(() -> "Introduction added by " + Thread.currentThread())
                        .thenApplyAsync(supplyAsyncResult -> {
                            logger.info(() -> " | Paragraph 1 added by " + Thread.currentThread());
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            logger.info(() -> " | Paragraph 1 added by " + Thread.currentThread());
                            return "";
                        });

        while (!cf.isDone()) {
            logger.info(()-> "random ..." + Thread.currentThread());
        }

        cf.get();
        /*
        CompletableFuture<Void> cfa
                = CompletableFuture.supplyAsync(() -> "Introduction added by " + Thread.currentThread())
                        .thenApplyAsync(supplyAsyncResult -> supplyAsyncResult + " | Paragraph 1 added by " + Thread.currentThread())
                        .thenApplyAsync(thenApplyResult -> thenApplyResult + " | Paragraph 2 added " + Thread.currentThread())
                        .thenAcceptAsync(thenApplyResult -> logger.info(() -> thenApplyResult + " | Summary added by " + Thread.currentThread()));
        cfa.get();        
        */
    }
}
