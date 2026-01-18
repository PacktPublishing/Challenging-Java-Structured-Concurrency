package challenge.concurrency;

import java.util.List;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Gatherers.mapConcurrent;
import java.util.stream.Stream;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Thread t = Thread.ofPlatform().start(() -> {
            List<Integer> results = Stream.iterate(1, n -> n + 1)
                    .limit(20)
                    .gather(mapConcurrent(3, x -> {                        
                        return processX(x);
                    }))
                    .collect(toList());

            logger.info(() -> "Results: " + results);
        });

        Thread.sleep(1000);
        logger.info(() -> "(Before calling interrupt()) Thread 't' is interrupted ? " + t.isInterrupted());
        t.interrupt();
        Thread.sleep(10000);
        logger.info(() -> "(After calling interrupt()) Thread 't' is interrupted ? " + t.isInterrupted());
        
        /*
        Thread t = Thread.ofPlatform().start(() -> {
            try (var scope = open(Joiner.<Integer>awaitAll())) {

                Stream.iterate(1, n -> n + 1)
                        .limit(20)
                        .map(n -> scope.fork(() -> processX(n)))                        
                        .collect(toList());

                scope.join();

                logger.info("Done");
            } catch (InterruptedException ex) {
                logger.severe("Scope owner thread was interrupted ...");
                Thread.currentThread().interrupt();
            }
        });        
        
        Thread.sleep(1000);
        logger.info(() -> "(Before calling interrupt()) Thread 't' is interrupted ? " + t.isInterrupted());
        t.interrupt();
        Thread.sleep(10000);
        logger.info(() -> "(After calling interrupt()) Thread 't' is interrupted ? " + t.isInterrupted());    
        */
    }

    public static int processX(int x) {

        LockSupport.parkNanos(1_000_000_000); // 1 second        
        logger.info(Thread.currentThread().toString());

        return x * 2;
    }
}
