package challenge.concurrency;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final List<Integer> listInts = List.of(5, 2, 9, 11, 12, 1, 3, 55, 3);

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        CompletableFuture<Integer>[] arrCf = new CompletableFuture[]{
            CompletableFuture.supplyAsync(() -> Collections.min(listInts)),
            CompletableFuture.supplyAsync(() -> Collections.max(listInts))
        };

        CompletableFuture.allOf(arrCf)
                .thenAccept(t -> Stream.of(arrCf)
                .map(CompletableFuture::join)
                .forEach(r -> logger.info(r.toString())));
    }
}
