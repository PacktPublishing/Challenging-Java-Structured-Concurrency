package challenge.concurrency;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public final class Conveyor {

    private Conveyor() {
        throw new AssertionError("Cannot have more than one conveyor!");
    }

    private static final Logger logger = Logger.getLogger(Conveyor.class.getName());
    private static final Random rnd = new Random();
    private static final BlockingQueue<String> batteryQueue = new LinkedBlockingQueue<>();
    
    private static final int MAXIMUM_NUMBER_DRAINERS = 50;
    private static final int MAXIMUM_SIZE_QUEUE = 5;
    private static final int CHARGERS = 3;
    private static final int DRAINERS = 2;
    private static final int MAXIMUM_CHARGING_TIME = 1 * 1000;
    private static final int MAXIMUM_DRAINING_TIME = 10 * 1000;
    private static final int INITIAL_QUEUE_DELAY = 5000;
    private static final int QUEUE_RATE = 3000;
    private static final int EXTRA_CHARGING_TIME = 4 * 1000;
    private static final int SLOW_DOWN_CHARGER = 150 * 1000;
    private static final int CONVEYOR_SHUTDOWN_TIME 
            = MAXIMUM_CHARGING_TIME + MAXIMUM_DRAINING_TIME + 1000;

    private static int extraChargingTime;   

    private static volatile boolean chargerOn;
    private static volatile boolean drainerOn;
    private static final Charger charger = new Charger();
    private static final Drainer drainer = new Drainer();

    private final static Semaphore chargerService = new Semaphore(CHARGERS);
    private final static Semaphore drainerService = new Semaphore(DRAINERS);

    private static ScheduledExecutorService watchService;
    private static ScheduledExecutorService downshiftService;

    private static final AtomicInteger nrOfDrainers = new AtomicInteger(DRAINERS);
    private static final AtomicBoolean removeDrainer = new AtomicBoolean();

    private static class Charger implements Runnable {

        @Override
        public void run() {
            while (chargerOn) {
                try {
                    String battery = "battery-" + rnd.nextInt(1000);

                    Thread.sleep(rnd.nextInt(MAXIMUM_CHARGING_TIME) + extraChargingTime);

                    batteryQueue.offer(battery);

                    logger.info(() -> "Charged: " + battery + " by charger: "
                            + Thread.currentThread().toString());
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    logger.severe(() -> "Charger Exception: " + ex);
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
                    String battery = batteryQueue.poll(
                            MAXIMUM_CHARGING_TIME + extraChargingTime, TimeUnit.MILLISECONDS);

                    if (battery != null) {
                        Thread.currentThread().sleep(rnd.nextInt(MAXIMUM_DRAINING_TIME));
                        logger.info(() -> "Drained: " + battery + " by drainer: "
                                + Thread.currentThread().toString());
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    logger.severe(() -> "Drainer Exception: " + ex);
                    break;
                }

                if (removeDrainer.get()) {

                    nrOfDrainers.decrementAndGet();
                    removeDrainer.set(false);
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void on() throws InterruptedException {

        if (chargerOn || drainerOn) {
            logger.info("Conveyor is already on ...");
            return;
        }

        logger.info("\n\nStarting conveyor ...");
        logger.info(() -> "Remaining batteries from previous shift: \n" + batteryQueue + "\n\n");

        chargerOn = true;
        for (int i = 0; i < CHARGERS; i++) {

            Thread.ofVirtual().start(() -> {
                try {
                    chargerService.acquire();
                } catch (InterruptedException ex) { /* handle exception */ }
                try {
                    charger.run();
                } finally {
                    chargerService.release();
                }
            });
        }

        drainerOn = true;
        for (int i = 0; i < DRAINERS; i++) {

            Thread.ofVirtual().start(() -> {
                try {
                    drainerService.acquire();
                } catch (InterruptedException ex) { /* handle exception */ }
                try {
                    drainer.run();
                } finally {
                    drainerService.release();
                }
            });
        }

        watchQueueSize();
        downshiftCharger();
    }

    private static void watchQueueSize() {
        
        watchService = Executors.newSingleThreadScheduledExecutor();
        watchService.scheduleAtFixedRate(() -> {

            if (batteryQueue.size() > MAXIMUM_SIZE_QUEUE
                    && nrOfDrainers.get() < MAXIMUM_NUMBER_DRAINERS) {

                addDrainer();
            } else {
                if (nrOfDrainers.get() > DRAINERS) {
                    removeDrainer();
                }
            }

            logger.warning(() -> "### Battery in batteryQueue: " + batteryQueue.size()
                    + " | Drainers waiting: " + drainerService.getQueueLength()
                    + " | Drainer available permits: " + drainerService.availablePermits()
                    + " | Running drainers: " + nrOfDrainers.get());
        }, INITIAL_QUEUE_DELAY, QUEUE_RATE, TimeUnit.MILLISECONDS);
    }

    public static void off() {

        logger.info("Stopping conveyor ...");

        chargerOn = false;
        drainerOn = false;

        downshiftSchedulers();
        
        logger.info("Conveyor off!");
    }

    private static void addDrainer() {

        logger.warning("### Adding a new drainer ...");

        if (drainerService.availablePermits() == 0) {
            drainerService.release();
        }

        Thread.ofVirtual().start(() -> {
            try {
                drainerService.acquire();
            } catch (InterruptedException ex) { /* handle exception */ }
            try {
                drainer.run();
            } finally {
                drainerService.release();
            }
        });

        nrOfDrainers.incrementAndGet();
    }

    private static void removeDrainer() {

        logger.warning("### Removing a drainer ...");

        removeDrainer.set(true);        
    }

    private static void downshiftCharger() {
        
        downshiftService = Executors.newSingleThreadScheduledExecutor();
        downshiftService.schedule(() -> {
            logger.warning("### Downshift chargers ...");
            extraChargingTime = EXTRA_CHARGING_TIME;
        }, SLOW_DOWN_CHARGER, TimeUnit.MILLISECONDS);
    }

    private static boolean downshiftSchedulers() {
        
        if (!chargerOn || !drainerOn) {
            return shutdownConveyorExecutor(watchService) && shutdownConveyorExecutor(downshiftService);
        }

        return false;
    }

    private static boolean shutdownConveyorExecutor(ExecutorService es) {
        es.shutdown();
        try {
            if (!es.awaitTermination(CONVEYOR_SHUTDOWN_TIME + extraChargingTime, TimeUnit.MILLISECONDS)) {
                es.shutdownNow();

                return es.awaitTermination(CONVEYOR_SHUTDOWN_TIME + extraChargingTime, TimeUnit.MILLISECONDS);
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