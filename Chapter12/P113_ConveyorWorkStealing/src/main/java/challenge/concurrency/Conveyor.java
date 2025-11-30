package challenge.concurrency;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public final class Conveyor {

    private Conveyor() {
        throw new AssertionError("Cannot have more than one conveyor!");
    }

    private static final Logger logger = Logger.getLogger(Conveyor.class.getName());

    private static final Random rnd = new Random();
    private static final Queue<String> batteryQueue = new ConcurrentLinkedQueue<>();

    private static final int AVAILABLE_PROCESSORS
            = Runtime.getRuntime().availableProcessors();

    private static final int MAXIMUM_CHARGED_BATTERIES = 15_000_000;

    private static final Drainer drainer = new Drainer();

    private static long drainersWorkStartTime;

    private static class Drainer implements Runnable {

        @Override
        public void run() {
            String battery = batteryQueue.poll();
            if (battery != null) {}
        }
    }

    public static void on() {

        mimicChargersWork();
        drainersAtWork_newWorkStealingPool();
        
        mimicChargersWork();
        drainersAtWork_newFixedThreadPool();
        
        mimicChargersWork();
        drainersAtWork_newVirtualThreadPerTaskExecutor();    
        
        mimicChargersWork();
        drainersAtWork_newCachedThreadPool();
    }

    private static void mimicChargersWork() {

        logger.info("");
        logger.info("Mimic the job of the chargers overnight ...");
        logger.info(() -> "Charged batteries: " + MAXIMUM_CHARGED_BATTERIES);

        for (int i = 0; i < MAXIMUM_CHARGED_BATTERIES; i++) {
            batteryQueue.offer("battery-" + rnd.nextInt(1000));
        }
    }

    private static void drainersAtWork_newWorkStealingPool() {

        logger.info(() -> "Available processors (workers): " + AVAILABLE_PROCESSORS);
        logger.info("Working via newWorkStealingPool() ...");

        int queueSize = batteryQueue.size();
                
        drainersWorkStartTime = System.nanoTime();
        try (ExecutorService drainerService = Executors.newWorkStealingPool()) {           

            for (int i = 0; i < queueSize; i++) {
                drainerService.execute(drainer);
            }
        }

        if (batteryQueue.isEmpty()) {
            long drainersWorkStopTime = System.nanoTime() - drainersWorkStartTime;

            logger.info(() -> "The drainers team finished their work via newWorkStealingPool() in "
                    + TimeUnit.SECONDS.convert(drainersWorkStopTime, TimeUnit.NANOSECONDS) + " seconds"
                    + " (" + TimeUnit.MILLISECONDS.convert(drainersWorkStopTime, TimeUnit.NANOSECONDS) + " ms)");
        }
    }
    
    private static void drainersAtWork_newFixedThreadPool() {

        logger.info(() -> "Available processors (workers): " + AVAILABLE_PROCESSORS);
        logger.info("Working via newFixedThreadPool() ...");

        int queueSize = batteryQueue.size();
                
        drainersWorkStartTime = System.nanoTime();
        try (ExecutorService drainerService = Executors.newFixedThreadPool(AVAILABLE_PROCESSORS)) {
        
            for (int i = 0; i < queueSize; i++) {
                drainerService.execute(drainer);
            }
        }

        if (batteryQueue.isEmpty()) {
            long drainersWorkStopTime = System.nanoTime() - drainersWorkStartTime;

            logger.info(() -> "The drainers team finished their work via newFixedThreadPool() in "
                    + TimeUnit.SECONDS.convert(drainersWorkStopTime, TimeUnit.NANOSECONDS) + " seconds"
                    + " (" + TimeUnit.MILLISECONDS.convert(drainersWorkStopTime, TimeUnit.NANOSECONDS) + " ms)");
        }
    }
    
    private static void drainersAtWork_newVirtualThreadPerTaskExecutor() {

        logger.info(() -> "Available processors (workers): " + AVAILABLE_PROCESSORS);
        logger.info("Working via newVirtualThreadPerTaskExecutor() ...");

        int queueSize = batteryQueue.size();
                
        drainersWorkStartTime = System.nanoTime();
        try (ExecutorService drainerService = Executors.newVirtualThreadPerTaskExecutor()) {
        
            for (int i = 0; i < queueSize; i++) {
                drainerService.execute(drainer);
            }
        }

        if (batteryQueue.isEmpty()) {
            long drainersWorkStopTime = System.nanoTime() - drainersWorkStartTime;

            logger.info(() -> "The drainers team finished their work via newVirtualThreadPerTaskExecutor() in "
                    + TimeUnit.SECONDS.convert(drainersWorkStopTime, TimeUnit.NANOSECONDS) + " seconds"
                    + " (" + TimeUnit.MILLISECONDS.convert(drainersWorkStopTime, TimeUnit.NANOSECONDS) + " ms)");
        }
    }
    
    private static void drainersAtWork_newCachedThreadPool() {

        logger.info(() -> "Available processors (workers): " + AVAILABLE_PROCESSORS);
        logger.info("Working via newCachedThreadPool() ...");

        int queueSize = batteryQueue.size();
                
        drainersWorkStartTime = System.nanoTime();
        try (ExecutorService drainerService = Executors.newCachedThreadPool()) {
        
            for (int i = 0; i < queueSize; i++) {
                drainerService.execute(drainer);
            }
        }

        if (batteryQueue.isEmpty()) {
            long drainersWorkStopTime = System.nanoTime() - drainersWorkStartTime;

            logger.info(() -> "The drainers team finished their work via newCachedThreadPool() in "
                    + TimeUnit.SECONDS.convert(drainersWorkStopTime, TimeUnit.NANOSECONDS) + " seconds"
                    + " (" + TimeUnit.MILLISECONDS.convert(drainersWorkStopTime, TimeUnit.NANOSECONDS) + " ms)");
        }
    }
}
