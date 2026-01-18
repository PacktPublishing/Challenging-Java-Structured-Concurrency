package challenge.concurrency;

import java.util.concurrent.Semaphore;
import java.util.concurrent.StructuredTaskScope;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final Semaphore washSpaces = new Semaphore(3, true); // 3 spaces for car washing

    private static void beforeWashing() {

        logger.info(() -> "Getting tokens ..." + Thread.currentThread());

        try {
            Thread.sleep((long) (Math.random() * 1000));
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private static void afterWashing() {

        logger.info(() -> "Free washing space ..." + Thread.currentThread()
                + " | waiting clients: " + washSpaces.getQueueLength());
    }

    private static void washCar() {

        beforeWashing();

        try {
            Thread.sleep((long) (Math.random() * 1500));
        } catch (InterruptedException ex) {} // washing car
        
        logger.info(() -> "Car washing ..." + Thread.currentThread());

        afterWashing();
    }

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        logger.info("Opening car wash ...");
        try (StructuredTaskScope scope = new StructuredTaskScope<String>()) {

            for (int i = 0; i < 100; i++) {
                Thread.sleep((long) (Math.random() * 500)); // cars arrives at car wash

                scope.fork(() -> {
                    if (washSpaces.getQueueLength() >= 15) { // we can accommodate around 15 parking spots
                        logger.info("Sorry, no parking left ...");
                    } else {
                        washSpaces.acquire();
                        try {
                            washCar();
                        } finally {
                            washSpaces.release();
                        }
                    }

                    return "Wash done";
                });
            }

            scope.join();
            
            logger.info("Closing car wash ...");                        
        }
    }
}
