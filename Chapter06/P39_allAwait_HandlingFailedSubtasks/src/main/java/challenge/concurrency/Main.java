package challenge.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import static java.util.concurrent.StructuredTaskScope.Subtask.State.FAILED;
import static java.util.concurrent.StructuredTaskScope.Subtask.State.SUCCESS;
import static java.util.concurrent.StructuredTaskScope.open;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        List<Integer> temps = weatherStationTemps();

        logger.info(temps.toString());
    }

    public static List<Integer> weatherStationTemps() throws InterruptedException {
     
        try (var scope = open(Joiner.<Integer>awaitAll())) {

            Subtask<Integer>[] subtasks = new Subtask[4];

            subtasks[0] = scope.fork(() -> weatherStationNorth());
            subtasks[1] = scope.fork(() -> weatherStationSouth());
            subtasks[2] = scope.fork(() -> weatherStationEast());
            subtasks[3] = scope.fork(() -> weatherStationWest());

            scope.join(); // Join subtasks, no exceptions are propagated

            // All subtasks have complete sucessfully or exceptionally
            List<Integer> temps = new ArrayList<>();

            for (Subtask<Integer> subtask : subtasks) {
                                
                switch (subtask.state()) {
                    case SUCCESS -> temps.add(subtask.get());
                    case FAILED -> {
                        if (subtask.exception() instanceof TemperatureException) {
                            temps.add(avgAnnualTemperature()); 
                        } else { 
                            throw new RuntimeException(subtask.exception());
                        }
                    }
                    default -> throw new IllegalStateException(subtask.state().toString()); // this should never happen
                }
            }

            return temps;
        }
    }

    public static int weatherStationNorth() {

        if (Math.random() < 0.5d) { throw new TemperatureException("North weather station exception"); }

        return ThreadLocalRandom.current().nextInt(20, 35);
    }

    public static int weatherStationSouth() {

        if (Math.random() < 0.5d) { throw new TemperatureException("South weather station exception"); }

        return ThreadLocalRandom.current().nextInt(25, 38);
    }

    public static int weatherStationWest() {

        if (Math.random() < 0.5d) { throw new TemperatureException("West weather station exception"); }

        return ThreadLocalRandom.current().nextInt(17, 32);
    }

    public static int weatherStationEast() {

        if (Math.random() < 0.5d) { throw new TemperatureException("East weather station exception"); }

        return ThreadLocalRandom.current().nextInt(27, 36);
    }
    
    // local service
    public static int avgAnnualTemperature() {
        return 25;
    }
}
