package challenge.concurrency;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import static java.util.concurrent.StructuredTaskScope.Subtask.State.SUCCESS;
import java.util.logging.Logger;

public class WeatherMap {

    private static final Logger logger = Logger.getLogger(WeatherMap.class.getName());
    
    private final Runnable weatherMap
            = () -> logger.info("Preparing the weather map based on temperatures, winds, and precipitations ...");
    
    public void buildWeatherMap(int i) throws InterruptedException, WeatherException {
        
        logger.info(() -> "\nFetching weather map parameters for day " + i);
     
        try (StructuredTaskScope scope = new StructuredTaskScope<String>()) {
                        
            Subtask<String> s1 = scope.fork(() -> { new WeatherParameters("Temperatures").fetchParameter(); return""; });
            Subtask<String> s2 = scope.fork(() -> { new WeatherParameters("Winds").fetchParameter(); return ""; });
            Subtask<String> s3 = scope.fork(() -> { new WeatherParameters("Precipitations").fetchParameter(); return ""; });
            
            scope.join();    
            
            if(s1.state().equals(SUCCESS) && s2.state().equals(SUCCESS) && s3.state().equals(SUCCESS)) {
                // barrier success
                weatherMap.run();
            } else {
                // barrier failed
                // handle FAILED subtasks
                throw new WeatherException();
            }
        }                                
    }
}