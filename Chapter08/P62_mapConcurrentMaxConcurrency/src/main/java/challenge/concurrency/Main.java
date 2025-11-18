package challenge.concurrency;

import java.util.logging.Logger;
import static java.util.stream.Gatherers.mapConcurrent;
import java.util.stream.Stream;

public class Main {
    
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    public static final int PROCESSORS = Runtime.getRuntime().availableProcessors();
    
    public static void main(String[] args) {                

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        logger.info("Max concurrency of 1:");
        Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .gather(mapConcurrent(1, x -> {
                    
                    try { Thread.sleep(3000); } catch (InterruptedException ex) {}
                    logger.info(() -> Thread.currentThread().toString());
                    
                    return x * 2;
                }))
                .forEach(x -> logger.info(() -> "New x: " + x));
        
        logger.info("");
        logger.info(() -> "\nMax concurrency of " + PROCESSORS + ":");
        Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .gather(mapConcurrent(PROCESSORS, x -> {
                    
                    try { Thread.sleep(3000); } catch (InterruptedException ex) {}
                    logger.info(() -> Thread.currentThread().toString());
                    
                    return x * 2;
                }))
                .forEach(x -> logger.info(() -> "New x: " + x));
    }
}
