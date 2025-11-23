package challenge.concurrency;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final Random rnd = new Random();

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");                        
                       
        // BadInterval interval = new BadInterval();
        GoodInterval interval = new GoodInterval();
        
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            
            for(int i = 0; i < 1_000_000; i++) {
                executor.submit(() -> interval.setRight(rnd.nextInt(0, 11)));                
                executor.submit(() -> interval.setLeft(rnd.nextInt(0, 11)));               
            }
        }
        logger.info(() -> "Interval: [" + interval.getLeft() +", " + interval.getRight() + "]");       
    }
}
