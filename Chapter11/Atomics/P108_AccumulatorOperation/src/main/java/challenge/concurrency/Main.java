package challenge.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");        
        
        logger.info("Executing the accumulator operation: ");
        AccumulatorOperation accTask = new AccumulatorOperation();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            
            for(int i = 0; i < 1_000_000; i++) {
                executor.submit(accTask);
            }
        }
        logger.info(() -> "Accumulator operation result: " + accTask.getCounter());
        
        logger.info("Executing the adder operation: ");
        AdderOperation addTask = new AdderOperation();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            
            for(int i = 0; i < 1_000_000; i++) {
                executor.submit(addTask);
            }
        }
        logger.info(() -> "Adder operation result: " + addTask.getCounter());
    }
}
