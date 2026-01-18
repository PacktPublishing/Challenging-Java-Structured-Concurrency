package challenge.concurrency;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
    private static final int DRAINERS = 0;
    
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

    private static ExecutorService chargerService;
    private static ExecutorService drainerService;

    private static ScheduledExecutorService watchService;
    private static ScheduledExecutorService downshiftService;

    private static final AtomicInteger nrOfDrainers = new AtomicInteger(DRAINERS);    
    private static final ThreadGroup drainersGroup = new ThreadGroup("drainers");

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
            while (drainerOn && !batteryQueue.isEmpty()
                    || nrOfDrainers.get() == 1) {

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
            }
            
            nrOfDrainers.decrementAndGet();
            logger.warning(() -> "### Drainer " + Thread.currentThread().getName()
                    + " will be released in 1 minute for now!");
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
        chargerService = Executors.newFixedThreadPool(CHARGERS);
        for(int i = 0; i < CHARGERS; i++) {
            chargerService.execute(charger);            
        }        

        drainerOn = true;
        drainerService = Executors.newCachedThreadPool(r -> new Thread(drainersGroup, r));
        
        nrOfDrainers.incrementAndGet();
        drainerService.execute(drainer);

        watchQueueSize();
        downshiftCharger();
    }

    private static void watchQueueSize() {
        
        watchService = Executors.newSingleThreadScheduledExecutor();
        watchService.scheduleAtFixedRate(() -> {

            if (batteryQueue.size() > MAXIMUM_SIZE_QUEUE
                    && drainersGroup.activeCount() < MAXIMUM_NUMBER_DRAINERS 
                    && nrOfDrainers.get() < MAXIMUM_NUMBER_DRAINERS) {

                logger.warning("### Adding a new drainer ...");
                
                nrOfDrainers.incrementAndGet();
                drainerService.execute(drainer);
            } 

            logger.warning(() -> "### Battery in batteryQueue: " + batteryQueue.size()
                    + " | Active drainer threads: " + drainersGroup.activeCount()                    
                    + " | Drainers: " + nrOfDrainers.get()
                    + " | Idle: " + (drainersGroup.activeCount() - nrOfDrainers.get()));
        }, INITIAL_QUEUE_DELAY, QUEUE_RATE, TimeUnit.MILLISECONDS);
    }

    public static void off() {

        logger.info("Stopping conveyor ...");

        boolean isChargerOff = chargerOff();
        boolean isDrainerOff = drainerOff();
        boolean isSchedulersOff = schedulersOff();

        if (!isChargerOff || !isDrainerOff || !isSchedulersOff) {
            logger.severe("Shutting down the conveyor caused some issues that should be investigated");
            System.exit(0);
        }
        
        logger.info("Conveyor off!");
    }    

    private static void downshiftCharger() {
        
        downshiftService = Executors.newSingleThreadScheduledExecutor();
        downshiftService.schedule(() -> {
            logger.warning("### Downshift chargers ...");
            extraChargingTime = EXTRA_CHARGING_TIME;
        }, SLOW_DOWN_CHARGER, TimeUnit.MILLISECONDS);
    }
    
    private static boolean chargerOff() {

        chargerOn = false;
        return shutdownConveyorExecutor(chargerService);
    }

    private static boolean drainerOff() {

        drainerOn = false;
        nrOfDrainers.set(0);
        return shutdownConveyorExecutor(drainerService);
    }

    private static boolean schedulersOff() {
        
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