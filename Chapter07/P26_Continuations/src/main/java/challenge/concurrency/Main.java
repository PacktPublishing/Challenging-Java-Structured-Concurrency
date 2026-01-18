package challenge.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());    
    
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable voidTask = () -> {
            logger.info(Thread.currentThread().toString());
            try {
                Thread.sleep((long) (Math.random()* 2000)); // between 0-2 seconds
            } catch (InterruptedException ex) {}
            logger.info(Thread.currentThread().toString());
        };
            
        try (ExecutorService executor = Executors
                .newVirtualThreadPerTaskExecutor()) {

            for (int i = 0; i < Runtime.getRuntime().availableProcessors() + 1; i++) {

                executor.submit(voidTask);                
            }
        }
    }
}