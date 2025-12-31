package challenge.concurrency;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public final class Conveyor {

    private Conveyor() {
        throw new AssertionError("Cannot have more than one conveyor!");
    }

    private static final Logger logger = Logger.getLogger(Conveyor.class.getName());
    private static final Random rnd = new Random();
    
    private static final Queue<String> batteryQueue = new ConcurrentLinkedQueue<>();
    private static final Queue<String> recycledQueue = new ConcurrentLinkedQueue<>();

    private static final int MAXIMUM_ROBOTIC_TERMINALS = 5;
    private static final int UNBOUNDED_BATTERY_QUEUE = 10_000_000; // unbounded queue
    private static final int MAXIMUM_CHARGING_TIME = 2 * 1000;
    private static final int MAXIMUM_DRAINING_TIME = 2 * 1000;
    private static final int CONVEYOR_SHUTDOWN_TIME
            = (MAXIMUM_CHARGING_TIME + MAXIMUM_DRAINING_TIME);

    private static volatile boolean chargerOn;
    private static volatile boolean drainerOn;

    private static ExecutorService chargerService;
    private static ExecutorService drainerService;

    private static class Charger implements Callable {

        private final String battery;

        private Charger(String battery) {
            this.battery = battery;
        }

        @Override
        public String call() throws ChargerDefectBatteryException, InterruptedException {

            if (chargerOn) {

                Thread.sleep(rnd.nextInt(MAXIMUM_CHARGING_TIME)); // simulate charging time

                if (rnd.nextInt(100) < 10) { // simulate defect battery
                    recycledQueue.offer(battery);
                    throw new ChargerDefectBatteryException("Charger report defect battery: " + battery);
                } else {
                    logger.info(() -> "Charged: " + battery + " by charger: "
                            + Thread.currentThread().toString());
                }

                return battery;
            }

            return "";
        }
    }

    private static class Drainer implements Callable {

        private final String battery;

        private Drainer(String battery) {
            this.battery = battery;
        }

        @Override
        public String call() throws DrainerDefectBatteryException, InterruptedException {

            if (drainerOn) {

                Thread.sleep(rnd.nextInt(MAXIMUM_DRAINING_TIME)); // simulate fast-discharging time

                if (rnd.nextInt(100) < 10) { // simulate defect battery
                    recycledQueue.offer(battery);
                    throw new DrainerDefectBatteryException("Drainer report defect battery: " + battery);
                } else {
                    logger.info(() -> "Drained: " + battery + " by drainer: "
                            + Thread.currentThread().toString());
                }
                return battery;
            }
            return "";
        }
    }

    @SuppressWarnings("unchecked")
    private static void rcs() {

        while (chargerOn && drainerOn) {

            String battery = batteryQueue.poll();
            Charger charger = new Charger(battery);

            Future<String> batteryChangerFuture = chargerService.submit(charger);

            try {
                String chargedBattery = batteryChangerFuture.get(
                        MAXIMUM_CHARGING_TIME + 1000, TimeUnit.MILLISECONDS);

                if (drainerOn) {
                    Drainer drainer = new Drainer(chargedBattery);
                    Future<String> batteryDrainerFuture = drainerService.submit(drainer);

                    String drainedBattery = batteryDrainerFuture.get(
                            MAXIMUM_DRAINING_TIME + 1000, TimeUnit.MILLISECONDS);

                    logger.info(() -> "Battery " + drainedBattery 
                            + " successfully tested by " + Thread.currentThread());
                }
            } catch (ExecutionException ex) {
                logger.severe(() -> "Battery rejected - " + ex.getCause());
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                logger.severe(() -> "Exception: " + ex);
            } catch (TimeoutException ex) {
                logger.severe("The worker doesn't respect the charging time!");
            }
        }
    }

    public static void on() {

        if (chargerOn || drainerOn) {
            logger.info("Conveyor is already on ...");
            return;
        }

        logger.info("\n\nFill up battery queue ...");
        
        fillBatteryQueue();
                
        logger.info("\n\nStarting conveyor ...");

        chargerOn = true;
        chargerService = Executors.newVirtualThreadPerTaskExecutor();

        drainerOn = true;
        drainerService = Executors.newVirtualThreadPerTaskExecutor();

        Thread.ofVirtual().name("RCS-thread").start(() -> {
            try (ExecutorService rcsService = Executors.newVirtualThreadPerTaskExecutor()) {
                for (int i = 0; i < MAXIMUM_ROBOTIC_TERMINALS; i++) {
                    rcsService.submit(() -> rcs());
                }
            }
        });
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
        
        logger.info(() -> "Defect batteries: " + recycledQueue);
    }
    
    private static void fillBatteryQueue() {
        
        for(int i = 0; i < UNBOUNDED_BATTERY_QUEUE; i++) {
            batteryQueue.offer("battery-" + rnd.nextInt(1000));
        }
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
