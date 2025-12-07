package challenge.concurrency;

import java.util.Random;
import java.util.logging.Logger;

public final class Conveyor {

    private Conveyor() {
        throw new AssertionError("Cannot have more than one conveyor!");
    }

    private static final Logger logger = Logger.getLogger(Conveyor.class.getName());
    private static final Random rnd = new Random();

    private static final int MAXIMUM_CHARGING_TIME = 2 * 1000;
    private static final int MAXIMUM_DRAINING_TIME = 2 * 1000;

    private static volatile boolean chargerOn;
    private static volatile boolean drainerOn;
 
    private static String battery;

    private static class ChargerDrainer {

        public static void charge() {
            if (chargerOn) {
                try {
                    battery = "battery-" + rnd.nextInt(1000);

                    Thread.sleep(rnd.nextInt(MAXIMUM_CHARGING_TIME)); // simulate charging time

                    logger.info(() -> "Charged: " + battery + " by charger: "
                            + Thread.currentThread().toString());

                    drain();

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.severe(() -> "Charger Exception: " + e);
                }
            }
        }

        public static void drain() {
            if (drainerOn) {
                try {

                    if (battery != null) {
                        Thread.sleep(rnd.nextInt(MAXIMUM_DRAINING_TIME)); // simulate fast-discharging time
                        logger.info(() -> "Drained: " + battery + " by drainer: "
                                + Thread.currentThread().toString());
                    }

                    charge();
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    logger.severe(() -> "Drainer Exception: " + ex);
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

        chargerOn = true;
        drainerOn = true;
        
        Thread.ofPlatform().start(() -> ChargerDrainer.drain());
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

        return !chargerOn;
    }

    private static boolean drainerOff() {

        drainerOn = false;

        return !drainerOn;
    }
}
