package challenge.concurrency;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable washCar = () -> {     
            try { Thread.sleep((long) (Math.random() * 1500)); } catch (InterruptedException ex) {} // washing car
            logger.info(() -> "Car washing ..." + Thread.currentThread());
        };

        logger.info("Opening car wash ...");
        try (SelfCarWashPoolExecutor executor = SelfCarWashPoolExecutor.newSelfCarWashPoolExecutor()) {
          
            for (int i = 0; i < 100; i++) {                
                Thread.sleep((long) (Math.random() * 500)); // cars arrives at car wash
                executor.submit(washCar);
            }
        }
        logger.info("Closing car wash ...");
    }
}