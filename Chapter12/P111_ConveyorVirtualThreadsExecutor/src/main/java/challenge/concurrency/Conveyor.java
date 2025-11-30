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
    
    private static final int CHARGERS = 3;
    private static final int DRAINERS = 2;
    private static final int MAXIMUM_CHARGING_TIME = 2 * 1000;
    private static final int MAXIMUM_DRAINING_TIME = 2 * 1000;
    private static final int CONVEYOR_SHUTDOWN_TIME 
            = (MAXIMUM_CHARGING_TIME + MAXIMUM_DRAINING_TIME) * (CHARGERS + DRAINERS);    

    private static volatile boolean chargerOn;
    private static volatile boolean drainerOn;
    
    private static final Charger charger = new Charger();
    private static final Drainer drainer = new Drainer();

    private static ExecutorService chargerService;
    private static ExecutorService drainerService;

    private static class Charger implements Runnable {

        @Override
        public void run() {
            while (chargerOn) {
                try {
                    String battery = "battery-" + rnd.nextInt(1000);

                    Thread.sleep(rnd.nextInt(MAXIMUM_CHARGING_TIME)); // simulate charging time

                    batteryQueue.offer(battery);

                    logger.info(() -> "Charged: " + battery + " by charger: "
                            + Thread.currentThread().toString());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.severe(() -> "Charger Exception: " + e);
                    break;
                }
            }
        }

    }

    private static class Drainer implements Runnable {

        @Override
        public void run() {
            while (drainerOn) {
                try {
                    String battery = batteryQueue.poll();

                    if (battery != null) {
                        Thread.sleep(rnd.nextInt(MAXIMUM_DRAINING_TIME)); // simulate fast-discharging time
                        logger.info(() -> "Drained: " + battery + " by drainer: "
                                + Thread.currentThread().toString());
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    logger.severe(() -> "Drainer Exception: " + ex);
                    break;
                }
            }
        }
    }

    public static void on() {

        if (chargerOn || drainerOn) {
            logger.info("Conveyor is already on ...");
            return;
        }

        logger.info("\n\nStarting conveyor ...");
        logger.info(() -> "Remaining batteries from previous shift: \n" + batteryQueue + "\n\n");

        chargerOn = true;
        // Executors.newFixedThreadPool(CHARGERS, Thread.ofVirtual().factory())
        // Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory())
        chargerService = Executors.newVirtualThreadPerTaskExecutor();
        for (int i = 0; i < CHARGERS; i++) {
            chargerService.execute(charger);
        }

        drainerOn = true;
        // Executors.newFixedThreadPool(DRAINERS, Thread.ofVirtual().factory())
        // Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory())
        drainerService = Executors.newVirtualThreadPerTaskExecutor();
        for (int i = 0; i < DRAINERS; i++) {
            drainerService.execute(drainer);
        }
    }

    public static void off() {

        logger.info("Stopping conveyor ...");

        boolean isChargerOff = chargerOff();
        boolean isDrainerOff = drainerOff();

        if (!isChargerOff || !isDrainerOff) {
            logger.severe("Shutting down the conveyor caused some issues that should be investigated");
            System.exit(0);
        }

        logger.info("Conveyor off!");
    }

    private static boolean chargerOff() {

        chargerOn = false;
        return shutdownConveyorExecutor(chargerService);
    }

    private static boolean drainerOff() {

        drainerOn = false;
        return shutdownConveyorExecutor(drainerService);
    }

    private static boolean shutdownConveyorExecutor(ExecutorService es) {
        es.shutdown();
        try {
            if (!es.awaitTermination(CONVEYOR_SHUTDOWN_TIME, TimeUnit.MILLISECONDS)) {
                es.shutdownNow();

                return es.awaitTermination(CONVEYOR_SHUTDOWN_TIME, TimeUnit.MILLISECONDS);
            }

            return true;
        } catch (InterruptedException e) {
            es.shutdownNow();
            Thread.currentThread().interrupt();
            logger.severe(() -> "Shutdown Exception: " + e);
        }
        return false;
    }
}
