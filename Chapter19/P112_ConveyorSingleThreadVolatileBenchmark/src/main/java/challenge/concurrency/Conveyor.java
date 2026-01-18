package challenge.concurrency;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.HdrHistogram.Histogram;

public final class Conveyor {

    private Conveyor() {
        throw new AssertionError("Cannot have more than one conveyor!");
    }

    private static final Logger logger = Logger.getLogger(Conveyor.class.getName());
    private static final Histogram latencyHistogram = new Histogram(TimeUnit.MINUTES.toNanos(1), 2);
    private static final Random rnd = new Random();

    private static final int MAXIMUM_CHARGING_TIME = 5;
    private static final int MAXIMUM_DRAINING_TIME = 5;

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

            long oldTime = System.nanoTime();
            while (chargerOn) {
                try {
                    while ((pingpong & 0x1) == 1) {     // while 'pingpong' is odd                   
                        Thread.onSpinWait();            // busy-waiting                         
                        // Thread.yield();
                        // Thread.sleep(1);
                    }

                    battery = "battery-" + rnd.nextInt(1000);

                    Thread.sleep(MAXIMUM_CHARGING_TIME); // simulate charging time

                    long newTime = System.nanoTime();
                    latencyHistogram.recordValue(newTime - oldTime);
                    oldTime = System.nanoTime();

                    pingpong++; // increment 'pingpong' to become even, so the consumer can work

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
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
                        Thread.onSpinWait();            // busy-waiting                        
                        // Thread.yield();
                        // Thread.sleep(1);
                    }

                    if (battery != null) {
                        Thread.sleep(MAXIMUM_DRAINING_TIME); // simulate fast-discharging time              
                    }

                    pingpong++; // increment 'pingpong' to become odd, so the producer can work
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
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

        latencyHistogram.reset();

        chargerOn = true;
        chargerThread = Thread.ofVirtual().name("charger-thread").start(charger);
        //chargerThread = Thread.ofPlatform().name("charger-thread").start(charger);

        drainerOn = true;
        drainerThread = Thread.ofVirtual().name("drainer-thread").start(drainer);
        //drainerThread = Thread.ofPlatform().name("drainer-thread").start(drainer);

        logger.info("\n\nConveyor on ...");
    }

    public static void off() throws IOException {

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

    public static void outputLatencyHistogram() throws IOException {
        latencyHistogram.outputPercentileDistribution(System.out, 5, 1.0);

        System.out.println("-> 50%:   " + latencyHistogram.getValueAtPercentile(50.0) + " ns");
        System.out.println("-> 90%:   " + latencyHistogram.getValueAtPercentile(90.0) + " ns");
        System.out.println("-> 99%:   " + latencyHistogram.getValueAtPercentile(99.0) + " ns");
        System.out.println("-> 99.9%: " + latencyHistogram.getValueAtPercentile(99.9) + " ns");

        latencyHistogram.reset();
    }
}
