package challenge.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import static java.util.concurrent.Future.State.SUCCESS;
import java.util.logging.Logger;

public class WeatherMap {

    private static final Logger logger = Logger.getLogger(WeatherMap.class.getName());

    private final Runnable weatherMap
            = () -> logger.info("Preparing the weather map based on temperatures, winds, and precipitations ...");

    public void buildWeatherMap(int i) throws InterruptedException, WeatherException {

        logger.info(() -> "\nFetching weather map parameters for day " + i);

        Future<?> f1, f2, f3;
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            f1 = executor.submit(() -> new WeatherParameters("Temperatures").fetchParameter());
            f2 = executor.submit(() -> new WeatherParameters("Winds").fetchParameter());
            f3 = executor.submit(() -> new WeatherParameters("Precipitations").fetchParameter());
        }

        if (f1.state().equals(SUCCESS) && f2.state().equals(SUCCESS) && f3.state().equals(SUCCESS)) {
            // barrier success
            weatherMap.run();
        } else {
            // barrier failed
            // handle FAILED tasks
            throw new WeatherException();
        }
    }
}
