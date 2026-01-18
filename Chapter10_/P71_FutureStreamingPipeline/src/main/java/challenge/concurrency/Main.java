package challenge.concurrency;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final Random rnd = new Random();
    
    private static Future<RGTennisRecord> queryRGTennisCourt(
            String court, ExecutorService executor) {
        
      return executor.submit(() -> {
            
            Thread.sleep((long) (Math.random() * 2000)); // simulate network request/response
            
            return new RGTennisRecord(court, "Player" + rnd.nextInt(100), 
                    "Player" + rnd.nextInt(100), "Set " + (1 + rnd.nextInt(3)) 
                            + ": " + rnd.nextInt(7) + "-" + rnd.nextInt(7));
        });
    }
    
    private static Future<CLFootballRecord> queryCLFootballStadium(
            String stadium, ExecutorService executor) {
        
        return executor.submit(() -> {
            
            Thread.sleep((long) (Math.random() * 2000)); // simulate network request/response
            
            return new CLFootballRecord(stadium, "Team" + rnd.nextInt(100), 
                    "Team" + rnd.nextInt(100), rnd.nextInt(6) + "-" + rnd.nextInt(6));
        });        
    }

    private static List<Future<RGTennisRecord>> fetchRGTennisScores(List<String> courts) {
        
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            
            return courts.stream()
                    .map(c -> queryRGTennisCourt(c, executor))
                    .toList();
        }
    }

    private static List<Future<CLFootballRecord>> fetchCLFootballScores(List<String> stadiums) {
        
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            
            return stadiums.stream()
                    .map(s -> queryCLFootballStadium(s, executor))
                    .toList();
        }
    }

    public static void main(String[] args) {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Future<List<Future<RGTennisRecord>>> tennisFuture;
        Future<List<Future<CLFootballRecord>>> footballFuture;

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            tennisFuture = executor.submit(() -> fetchRGTennisScores(
                    List.of("Philippe Chatrier", "Suzanne Lenglen", "Simonne Mathieu")));
            
            footballFuture = executor.submit(() -> fetchCLFootballScores(
                    List.of("Wembley", "Old Trafford", "Estadio da Luz", "Allianz Arena", 
                            "San Siro", "Stadion Feijenoord", "Karaiskakis Stadium", 
                            "Luzhniki Stadium", "Camp Nou", "Stade de France")));
        }
        
        tennisFuture.resultNow()
                .forEach(r -> logger.info(() -> r.resultNow().toString()));
        
        footballFuture.resultNow()
                .forEach(r -> logger.info(() -> r.resultNow().toString()));
    }
}
