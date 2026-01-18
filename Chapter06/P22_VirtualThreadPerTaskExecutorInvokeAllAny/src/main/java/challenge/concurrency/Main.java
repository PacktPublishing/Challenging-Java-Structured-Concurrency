package challenge.concurrency;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            int gate = executor.invokeAny(
                    List.of(() -> 1, () -> 2, () -> 3)
            );
            
            logger.info(() -> "Open gate number: " + String.valueOf(gate));
        }
        
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            List<Future<Integer>> gates = executor.invokeAll(
                    List.of(() -> 1, () -> 2, () -> 3)
            );
            
            gates.forEach(g -> logger.info(() -> "Open gate state: " + g.state()));
        }                
    }
}