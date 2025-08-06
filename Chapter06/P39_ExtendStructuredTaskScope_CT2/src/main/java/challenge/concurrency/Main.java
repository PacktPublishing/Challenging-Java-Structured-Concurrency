package challenge.concurrency;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final Random rnd = new Random();
    
    public static final int STATIONS = 10;

    private static double station(int station) throws InterruptedException {
                
        Thread.sleep(rnd.nextInt(3000)); // simulate network  
        
        logger.info(() -> "Station: " + station + " - Thread: " + Thread.currentThread().toString());
        
        return rnd.nextDouble(-50, 51);
    }        

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        double avgTemperature = temperatureStation(STATIONS);        
        logger.info(() -> "Avg temperature: " + avgTemperature);
    }  

    public static double temperatureStation(int stations) throws InterruptedException {
        
        try (WeatherTaskScope scope = new WeatherTaskScope()) {

            for (int i = 0; i < stations; i++) {
                int index = i;
                scope.fork(() -> station(index));
            }

            scope.join();

            return scope.averageTemperature();
        }
    }
}