package challenge.concurrency;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.StructuredTaskScope.FailedException;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void fastTask() {

        logger.info("Sleep 5 seconds (fast task) ...");
        try {
            Thread.sleep(5000);
            logger.info("Done sleeping 5 seconds (fast task) ...");
        } catch (InterruptedException ex) {
            logger.info("Fast task was interrupted ...");
            Thread.currentThread().interrupt();
        }
    }

    public static void slowTask() {
        logger.info("Sleep 10 seconds (slow task) ...");
        try {
            Thread.sleep(10000);
            logger.info("Done sleeping 10 seconds (slow task) ...");
        } catch (InterruptedException ex) {
            logger.info("Slow task was interrupted ...");
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        try (var fastScope = StructuredTaskScope.open()) {

            Subtask fastSubtask = fastScope.fork(() -> fastTask());

            try (var slowScope = StructuredTaskScope.open()) {

                Subtask slowSubtask = slowScope.fork(() -> slowTask());

                Subtask additionalSubtask = slowScope.fork(() -> {
                    Thread.sleep(3000); // this is faster than the fast task
                    throw new RuntimeException("Additional task failed ...");
                });

                slowScope.join();

                logger.info(() -> "Slow subtask state:" + slowSubtask.state());
                logger.info(() -> "Additional subtask state:" + additionalSubtask.state());
            } catch (FailedException e) {} // handle this exception

            fastScope.join();

            logger.info(() -> "Fast subtask state:" + fastSubtask.state());
        }
    }
}
