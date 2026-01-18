package challenge.concurrency;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import static java.util.concurrent.Future.State.FAILED;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            List<Throwable> wifi1Ex = executor.invokeAll(
                    List.of(() -> "wifi-A", () -> "wifi-B", () -> "wifi-C".substring(25)))
                    .stream()
                    .filter(t -> t.state() == FAILED)
                    .<Throwable>mapMulti((t, r) -> {
                        r.accept((Throwable) t.exceptionNow());
                    }).collect(toList());

            List<Throwable> wifi2Ex = executor.invokeAll(
                    List.of(() -> "wifi-A", () -> "wifi-B".codePointAt(10), () -> "wifi-C"))
                    .stream()
                    .filter(t -> t.state() == FAILED)
                    .map(Future::exceptionNow)
                    .toList();

            logger.severe(wifi1Ex.toString());
            logger.severe(wifi2Ex.toString());
        }
    }
}
