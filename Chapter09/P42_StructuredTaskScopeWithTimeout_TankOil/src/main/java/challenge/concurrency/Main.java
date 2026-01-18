package challenge.concurrency;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.TimeoutException;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final Random rnd = new Random();

    interface SensorCallback {

        void onMeasurement(String value);
    }

    static final class PressureSensor {

        private final SensorCallback sensorCallback;

        public PressureSensor(SensorCallback sensorCallback) {
            this.sensorCallback = sensorCallback;
        }

        public void pingMeasurement() {            
            try {
                while (true) {                    
                    Thread.sleep(1000);
                    sensorCallback.onMeasurement("Tank pressure: " + rnd.nextInt(1, 6));
                }
            } catch (InterruptedException e) {
                logger.info("Pressure sensor interrupted...");
                Thread.currentThread().interrupt();
            }
        }
    }
    
    static final class TemperatureSensor {

        private final SensorCallback sensorCallback;

        public TemperatureSensor(SensorCallback sensorCallback) {
            this.sensorCallback = sensorCallback;
        }

        public void pingMeasurement() {            
            try {
                while (true) {
                    Thread.sleep(1000);
                    sensorCallback.onMeasurement("Tank temperature: " + rnd.nextInt(60, 120));
                }
            } catch (InterruptedException e) {
                logger.info("Temperature sensor interrupted...");
                Thread.currentThread().interrupt();
            }
        }
    }
    
    static final class VolumeSensor {

        private final SensorCallback sensorCallback;

        public VolumeSensor(SensorCallback sensorCallback) {
            this.sensorCallback = sensorCallback;
        }

        public void pingMeasurement() {            
            try {
                while (true) {
                    Thread.sleep(1000);
                    sensorCallback.onMeasurement("Tank volume: " + rnd.nextInt(10000, 11000));
                }
            } catch (InterruptedException e) {
                logger.info("Volume sensor interrupted...");
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

      monitorTankParameters(Duration.ofSeconds(5));

    }

    public static void monitorTankParameters(Duration timeout) throws InterruptedException {
       
        try (var scope = StructuredTaskScope.open(Joiner.<Void>awaitAll(),
                cf -> cf.withTimeout(timeout))) {

            logger.info(() -> "Monitor tank parameters for " 
                    + timeout.getSeconds() + " seconds ...");
            
            scope.fork(() -> new PressureSensor(
                    result -> logger.info(result)).pingMeasurement());
            scope.fork(() -> new TemperatureSensor(
                    result -> logger.info(result)).pingMeasurement());
            scope.fork(() -> new VolumeSensor(
                    result -> logger.info(result)).pingMeasurement());

            scope.join(); // Join subtasks, propagating exceptions
                    
        } catch (TimeoutException ex) {
            logger.info("Monitor tank parameters timeout ...");
        }
    }
}
