package challenge.concurrency;

import java.time.Duration;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Logger;

public class WeatherParameters {

    private static final Logger logger = Logger.getLogger(WeatherParameters.class.getName());

    private final String parameter;
    private final CyclicBarrier cb;

    public WeatherParameters(String parameter, CyclicBarrier cb) {
        this.parameter = parameter;
        this.cb = cb;
    }
    
    public void fetchParameter() {

        int fetchInAround = (int) (Math.random() * 10);

        try {
            logger.info(() -> "Fetching parameter: '" + parameter + "'");
            Thread.sleep(Duration.ofSeconds(fetchInAround));

            logger.info(() -> "Parameter '"
                    + parameter + "' fetched in " + fetchInAround + " seconds ");

            cb.await();

        } catch (InterruptedException | BrokenBarrierException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
