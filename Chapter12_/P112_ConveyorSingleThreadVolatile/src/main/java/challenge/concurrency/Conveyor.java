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
    private static volatile long pingpong = 1; // initially odd

    private static final Charger charger = new Charger();
    private static final Drainer drainer = new Drainer();

    private static Thread chargerThread; // producer thread
    private static Thread drainerThread; // consumer thread

    private static String battery;

    private static class Charger implements Runnable {

        @Override
        public void run() {
            while (chargerOn) {
                try {
                    while ((pingpong & 0x1) == 1) {     // while 'pingpong' is odd
                      Thread.onSpinWait();              // busy-waiting                      
                    }

                    battery = "battery-" + rnd.nextInt(1000);

                    Thread.sleep(rnd.nextInt(MAXIMUM_CHARGING_TIME)); // simulate charging time

                    logger.info(() -> "Charged: " + battery + " by charger: "
                            + Thread.currentThread().toString());

                    pingpong++; // increment 'pingpong' to become even, so the consumer can work

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
                    while ((pingpong & 0x1) == 0) {     // while 'pingpong' is even
                      Thread.onSpinWait();              // busy-waiting                      
                    }

                    if (battery != null) {
                        Thread.sleep(rnd.nextInt(MAXIMUM_DRAINING_TIME)); // simulate fast-discharging time
                        logger.info(() -> "Drained: " + battery + " by drainer: "
                                + Thread.currentThread().toString());
                    }

                    pingpong++; // increment 'pingpong' to become odd, so the producer can work
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

        chargerOn = true;
        chargerThread = Thread.ofVirtual().name("charger-thread").start(charger);

        drainerOn = true;
        drainerThread = Thread.ofVirtual().name("drainer-thread").start(drainer);
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
        chargerThread.interrupt();

        return !chargerOn && chargerThread.isInterrupted();
    }

    private static boolean drainerOff() {

        drainerOn = false;
        drainerThread.interrupt();

        return !drainerOn && drainerThread.isInterrupted();
    }
}
