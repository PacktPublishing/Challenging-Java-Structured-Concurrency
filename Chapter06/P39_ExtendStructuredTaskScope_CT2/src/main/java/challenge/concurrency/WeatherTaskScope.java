package challenge.concurrency;

import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.StructuredTaskScope;
import java.util.logging.Logger;

public class WeatherTaskScope extends StructuredTaskScope<Double> {

    private static final Logger logger = Logger.getLogger(WeatherTaskScope.class.getName());
    
    private final List<Double> temperatures = new CopyOnWriteArrayList<>();

    @Override
    protected void handleComplete(Subtask<? extends Double> subtask) {

        switch (subtask.state()) {
            case SUCCESS ->
                temperatures.add(subtask.get());                       
        }
    }

    public double averageTemperature() {
        
        logger.info(() -> "Results: " + temperatures.toString());
        
        OptionalDouble average = temperatures
            .stream()
            .mapToDouble(t -> t)
            .average();
        
       return average.isPresent() ? average.getAsDouble() : 0.0d; 
    }
}
