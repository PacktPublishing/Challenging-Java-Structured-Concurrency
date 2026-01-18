package challenge.concurrency;

import java.util.concurrent.CyclicBarrier;
import java.util.logging.Logger;

public class WeatherMap {

    private static final Logger logger = Logger.getLogger(WeatherMap.class.getName());

    private final Runnable weatherMap
            = () -> logger.info("Preparing the weather map based on temperatures, winds, and precipitations ...");

    private final CyclicBarrier cb = new CyclicBarrier(3, weatherMap);

    public void buildWeatherMap(int i) throws InterruptedException {

        logger.info(() -> "\nFetching weather map parameters for day " + i);
        
        Thread tempThread = Thread.ofVirtual().unstarted(
                () -> new WeatherParameters("Temperatures", cb).fetchParameter());
        Thread windThread = Thread.ofVirtual().unstarted(
                () -> new WeatherParameters("Winds", cb).fetchParameter());
        Thread prepThread = Thread.ofVirtual().unstarted(
                () -> new WeatherParameters("Precipitations", cb).fetchParameter());

        tempThread.start();
        windThread.start();
        prepThread.start();

        // wait for threads to finish
        tempThread.join();
        windThread.join();
        prepThread.join();       
    }
}
