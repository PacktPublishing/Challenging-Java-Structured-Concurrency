package challenge.concurrency;

import java.time.Duration;
import java.util.logging.Logger;

public class WeatherParameters {

    private static final Logger logger = Logger.getLogger(WeatherParameters.class.getName());

    private final String parameter;
  
    public WeatherParameters(String parameter) {
        this.parameter = parameter;
    }
    
    public void fetchParameter() {

        int fetchInAround = (int) (Math.random() * 10);
        
        logger.info(() -> "Fetching parameter: '" + parameter + "'");
        
        try { Thread.sleep(Duration.ofSeconds(fetchInAround)); } catch (InterruptedException ex) {}

        logger.info(() -> "Parameter '"
                    + parameter + "' fetched in " + fetchInAround + " seconds");          
    }
}