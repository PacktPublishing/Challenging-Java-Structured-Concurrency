package challenge.concurrency;

import static challenge.concurrency.Main.temperatureSensors;
import java.util.List;
import java.util.Random;
import java.util.concurrent.StructuredTaskScope;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final Random rnd = new Random();
    private static final int MAX_SENSORS = 10000;
   
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");    
        
        List<String> sensorResults = temperatureSensors(MAX_SENSORS);
        
        logger.info(() -> "Available sensors: " + sensorResults.size() + " of " + MAX_SENSORS);
        logger.info(sensorResults.toString());
    }

    public static List<String> temperatureSensors(int sensors) throws InterruptedException {

        try (var scope = StructuredTaskScope.open(new SensorTaskScope())) {

            for (int i = 0; i < sensors; i++) {
                int index = i;
                scope.fork(() -> sensor(index));
            }

            return scope.join();
        }
    }
    
    public static String sensor(int index) {
        
        if (Math.random() < 0.05d) { 
            logger.severe(() -> "Sensor " + index + " unavailable");
            throw new SensorException("Sensor " + index + " unavailable"); 
        }
        
        try { Thread.sleep((long) (Math.random() * 5)); } catch (InterruptedException ex) {}
      
       return "Sensor " + index + " Temperature " + rnd.nextInt(-50, 50);
    }
}
