package challenge.concurrency;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
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

    private static volatile boolean chargerOn;
    private static volatile boolean drainerOn;

    private static final Charger charger = new Charger();
    private static final Drainer drainer = new Drainer();

    private static final Semaphore chargerService = new Semaphore(CHARGERS);
    private static final Semaphore drainerService = new Semaphore(DRAINERS);

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
    }

    public static void off() {

        logger.info("Stopping conveyor ...");

        chargerOn = false;
        drainerOn = false;

        logger.info("Conveyor off!");
    }
}
