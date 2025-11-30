package challenge.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        CompletableFuture<Void> cf
                = CompletableFuture.supplyAsync(() -> "Email To foo@gmail.com")
                        .thenRun(() -> logger.info(() -> "E-mail sent by " + Thread.currentThread()));

        cf.get();
    }
}
