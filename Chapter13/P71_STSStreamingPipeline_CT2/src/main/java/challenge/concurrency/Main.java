package challenge.concurrency;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final Random rnd = new Random();

    private static Subtask<RGTennisRecord> queryRGTennisCourt(
            String court, StructuredTaskScope scope) {

        return scope.fork(() -> {

            Thread.sleep((long) (Math.random() * 2000)); // simulate network requst/response

            return new RGTennisRecord(court, "Player" + rnd.nextInt(100),
                    "Player" + rnd.nextInt(100), "Set " + (1 + rnd.nextInt(3))
                    + ": " + rnd.nextInt(7) + "-" + rnd.nextInt(7));
        });
    }

    private static Subtask<CLFootballRecord> queryCLFootballStadium(
            String stadium, StructuredTaskScope scope) {

        return scope.fork(() -> {

            Thread.sleep((long) (Math.random() * 2000)); // simulate network requst/response

            return new CLFootballRecord(stadium, "Team" + rnd.nextInt(100),
                    "Team" + rnd.nextInt(100), rnd.nextInt(6) + "-" + rnd.nextInt(6));
        });
    }

    private static List<Subtask<RGTennisRecord>>
            fetchRGTennisScores(List<String> courts) throws InterruptedException {

        try (var scope = StructuredTaskScope.open(Joiner.<List<Subtask<RGTennisRecord>>>awaitAll())) {

            List<Subtask<RGTennisRecord>> tennisResults = courts.stream()
                    .map(c -> queryRGTennisCourt(c, scope))
                    .toList();

            scope.join();

            return tennisResults;
        }
    }

    private static List<Subtask<CLFootballRecord>>
            fetchCLFootballScores(List<String> stadiums) throws InterruptedException {

        try (var scope = StructuredTaskScope.open(Joiner.<List<Subtask<CLFootballRecord>>>awaitAll())) {

            List<Subtask<CLFootballRecord>> footballResults = stadiums.stream()
                    .map(s -> queryCLFootballStadium(s, scope))
                    .toList();

            scope.join();

            return footballResults;
        }
    }

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        ScheduledExecutorService executorsch = Executors.newScheduledThreadPool(1);
        executorsch.scheduleAtFixedRate(() -> {
            try (var scope = StructuredTaskScope.open(Joiner.<Object>awaitAll())) {

                Subtask<List<Subtask<RGTennisRecord>>> tennisSubtask = scope.fork(() -> fetchRGTennisScores(
                        List.of("Philippe Chatrier", "Suzanne Lenglen", "Simonne Mathieu")));

                Subtask<List<Subtask<CLFootballRecord>>> footballSubtask = scope.fork(() -> fetchCLFootballScores(
                        List.of("Wembley", "Old Trafford", "Estadio da Luz", "Allianz Arena",
                                "San Siro", "Stadion Feijenoord", "Karaiskakis Stadium",
                                "Luzhniki Stadium", "Camp Nou", "Stade de France")));

                scope.join();

                tennisSubtask.get()
                        .forEach(r -> logger.info(r.get().toString()));
                footballSubtask.get()
                        .forEach(r -> logger.info(r.get().toString()));
            } catch (InterruptedException ex) {
            }
        }, 3, 3, TimeUnit.SECONDS);
    }
}
