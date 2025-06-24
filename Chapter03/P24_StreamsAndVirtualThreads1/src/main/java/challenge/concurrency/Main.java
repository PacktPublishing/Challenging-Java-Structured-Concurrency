package challenge.concurrency;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            IntStream.range(0, 5)
                    .mapToObj(t -> executor.submit(() -> {
                return Thread.currentThread().toString() + "(" + t + ")";
            })).forEach(t -> {
                try { logger.info(t.get()); } catch (InterruptedException | ExecutionException ex) {}
            });
            
            List<String> strsValues = IntStream.range(0, 5)
                    .mapToObj(t -> executor.submit(() -> {
                return Thread.currentThread().toString() + "(" + t + ")";
            })).map(t -> {
                try { return t.get(); } catch (InterruptedException | ExecutionException ex) {}                
                return "";
            }).collect(toList());                    
                        
            List<Future<String>> strsFutures = IntStream.range(0, 5)
                    .mapToObj(t -> executor.submit(() -> {
                return Thread.currentThread().toString() + "(" + t + ")";
            })).collect(toList());
            
            Future<String> f1 = executor.submit(() -> "wifi-A");
            Future<String> f2 = executor.submit(() -> "wifi-B");
            Future<String> f3 = executor.submit(() -> "wifi-C");
            
            String strs = Stream.of(f1.get(), f2.get(), f3.get())
                    .collect(joining(", ", "[", "]"));
            
            logger.info(strs);
        }
    }
}
