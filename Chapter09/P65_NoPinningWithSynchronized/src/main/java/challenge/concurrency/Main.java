package challenge.concurrency;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void runTask() { // public static synchronized void runTask() {
        
        logger.info("Before sleep ...");
        try { Thread.sleep((long) (Math.random() * 5000)); } catch (InterruptedException ex) {}
        logger.info("After sleep ...");
    }
  
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        try (ExecutorService executor = Executors.newThreadPerTaskExecutor(
                Thread.ofVirtual().name("VirtualThread-", 0).factory())) {

            for (int i = 0; i < 10; i++) {
                executor.submit(() -> runTask());
            }
        }
    }
}
