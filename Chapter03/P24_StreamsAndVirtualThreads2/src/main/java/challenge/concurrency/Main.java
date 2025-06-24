package challenge.concurrency;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import static java.util.concurrent.Future.State.SUCCESS;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            List<String> wifi1 = executor.invokeAll(
                    List.of(() -> "wifi-A", () -> "wifi-B", () -> "wifi-C"))
                    .stream()
                    .filter(t -> t.state() == SUCCESS)
                    .<String>mapMulti((t, r) -> {
                        r.accept((String) t.resultNow());
                    }).collect(toList());

            List<String> wifi2 = executor.invokeAll(
                    List.of(() -> "wifi-A", () -> "wifi-B", () -> "wifi-C"))
                    .stream()
                    .filter(t -> t.state() == SUCCESS)
                    .map(t -> t.resultNow().toString())
                    .toList();

            List<Object> wifi3 = executor.invokeAll(
                    List.of(() -> "wifi-A", () -> "wifi-B", () -> "wifi-C"))
                    .stream()
                    .filter(t -> t.state() == SUCCESS)
                    .map(Future::resultNow)
                    .toList();

            logger.info(() -> "wifi1: " + wifi1.toString());
            logger.info(() -> "wifi2: " + wifi2.toString());
            logger.info(() -> "wifi3: " + wifi3.toString());
        }
    }
}