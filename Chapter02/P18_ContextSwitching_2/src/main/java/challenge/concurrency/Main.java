package challenge.concurrency;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        System.setProperty("jdk.virtualThreadScheduler.parallelism", "1");
        System.setProperty("jdk.virtualThreadScheduler.maxPoolSize", "1");
        System.setProperty("jdk.virtualThreadScheduler.minRunnable", "1");

        Runnable fiveSleepTask = () -> {
            logger.info(() -> Thread.currentThread().toString() + " | preparing to sleep 5 seconds");            
            logger.info(() -> Thread.currentThread().toString() + " | sleeping (blocking)");
            try { Thread.sleep(5000); } catch (InterruptedException ex) {} // blocking            
            logger.info(() -> Thread.currentThread().toString() + " | sleep done");
        };
        
        Runnable oneSleepTask = () -> {
            logger.info(() -> Thread.currentThread().toString() + " | preparing to sleep 1 second");            
            logger.info(() -> Thread.currentThread().toString() + " | sleeping (blocking)");
            try { Thread.sleep(1000); } catch (InterruptedException ex) {} // blocking            
            logger.info(() -> Thread.currentThread().toString() + " | sleep done");
        };

        Thread vtThread1 = Thread.ofVirtual()
                .name("sleepFive").start(fiveSleepTask);
        Thread vtThread2 = Thread.ofVirtual()
                .name("sleepOne").start(oneSleepTask);
        
        vtThread1.join();
        vtThread2.join();
    }
}