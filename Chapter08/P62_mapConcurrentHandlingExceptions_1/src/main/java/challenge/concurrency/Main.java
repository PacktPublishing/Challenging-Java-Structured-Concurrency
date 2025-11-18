package challenge.concurrency;

import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Gatherers.mapConcurrent;
import java.util.stream.Stream;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    public static void main(String[] args) {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        long count = 0;
        try {
            count = Stream.of(1, 2, -3, 4, 5, 6, 7, 8, 9, 10)
                    .gather(mapConcurrent(4, x -> {
                        try { Thread.sleep(1000); } catch (InterruptedException ex) {}
                        logger.info(() -> "Processing " + x + " by " + Thread.currentThread().toString());

                        if (x < 0) {
                            throw new IllegalArgumentException("Negative numbers are not allowed");
                        }

                        return x * 2;
                    }))
                    .count();

        } catch (IllegalArgumentException e) {
            logger.severe(e.toString());
        }

        logger.log(Level.INFO, "Processed: {0}", count); // 0, in case of exception
    }
}
