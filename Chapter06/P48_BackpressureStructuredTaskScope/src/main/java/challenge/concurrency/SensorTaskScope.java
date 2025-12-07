package challenge.concurrency;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.StructuredTaskScope;

public class SensorTaskScope extends BackpressureTaskScope<String, List<String>> {

    private final List<String> results = new CopyOnWriteArrayList<>();
    private final List<Throwable> exceptions = new CopyOnWriteArrayList<>();
    
    @Override
    public boolean onComplete(StructuredTaskScope.Subtask<String> subtask) {
        
        switch (subtask.state()) {
            case SUCCESS -> 
                results.add(subtask.get());                            
            case FAILED -> {
                exceptions.add(subtask.exception());
                if (exceptions.size() >= 5) {
                    BackpressureTaskScope.backpressure = Integer.MAX_VALUE; // reset backpressure
                    return true;
                }
            }
            case UNAVAILABLE -> {
                throw new IllegalStateException("Subtask may still running ...");
            }
        }
                
        return super.onComplete(subtask);
    }
    
     @Override
    public List<String> result() throws Throwable {

        return results;
    }
}
