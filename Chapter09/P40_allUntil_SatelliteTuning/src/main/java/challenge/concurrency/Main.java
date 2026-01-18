package challenge.concurrency;

import java.util.List;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import static java.util.concurrent.StructuredTaskScope.Subtask.State.SUCCESS;
import static java.util.concurrent.StructuredTaskScope.open;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    private static class SatelliteTuningFailures<String> 
            implements Predicate<Subtask<String>> {
        
        private final AtomicInteger frequencyFailedCount = new AtomicInteger();
        
        @Override
        public boolean test(Subtask<String> subtask) {
                                
            return subtask.state() == Subtask.State.FAILED
                    && frequencyFailedCount.incrementAndGet() >= 2;
        }
    }

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        List<String> frequencies = satelliteTuning();
        logger.info(frequencies.toString());
    }

    public static List<String> satelliteTuning() throws InterruptedException {
     
        try (var scope = open(Joiner.<String>allUntil(new SatelliteTuningFailures<>()))) {
           
            scope.fork(() -> satelliteFrequency12GHz());
            scope.fork(() -> satelliteFrequency24GHz());
            scope.fork(() -> satelliteFrequency48GHz());
            scope.fork(() -> satelliteFrequency1218GHz());
            scope.fork(() -> satelliteFrequency2432GHz());

            List<Subtask<String>> results = scope.join(); // Join subtasks, no exceptions are propagated                                        
                            
            results.forEach(r -> logger.info(r.state().toString()));
                        
            return results.stream()
                    .filter(r -> r.state() == SUCCESS)
                    .map(r -> r.get())
                    .toList();                                
        }
    }

    public static String satelliteFrequency12GHz() {

        if (Math.random() < 0.3d) { throw new FrequencyException("Frequency 1-2 GHz unavailable"); }
        
        try { Thread.sleep((long) (Math.random() * 2000)); } catch (InterruptedException ex) {}

        return ThreadLocalRandom.current().nextInt(1, 3) + "GHz";
    }

    public static String satelliteFrequency24GHz() {

        if (Math.random() < 0.3d) { throw new FrequencyException("Frequency 2-4 GHz unavailable"); }
        
        try { Thread.sleep((long) (Math.random() * 2000)); } catch (InterruptedException ex) {}

        return ThreadLocalRandom.current().nextInt(2, 5) + "GHz";
    }
    
    public static String satelliteFrequency48GHz() {

        if (Math.random() < 0.3d) { throw new FrequencyException("Frequency 4-8 GHz unavailable"); }
        
        try { Thread.sleep((long) (Math.random() * 2000)); } catch (InterruptedException ex) {}

        return ThreadLocalRandom.current().nextInt(4, 9) + "GHz";
    }
    
    public static String satelliteFrequency1218GHz() {

        if (Math.random() < 0.3d) { throw new FrequencyException("Frequency 12-18 GHz unavailable"); }
        
        try { Thread.sleep((long) (Math.random() * 2000)); } catch (InterruptedException ex) {}

        return ThreadLocalRandom.current().nextInt(12, 19) + "GHz";
    }
    
    public static String satelliteFrequency2432GHz() {

        if (Math.random() < 0.3d) { throw new FrequencyException("Frequency 24-32 GHz unavailable"); }
        
        try { Thread.sleep((long) (Math.random() * 2000)); } catch (InterruptedException ex) {}

        return ThreadLocalRandom.current().nextInt(24, 33) + "GHz";
    }
}
