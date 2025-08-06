package challenge.concurrency;

import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import static java.util.concurrent.StructuredTaskScope.Subtask.State.SUCCESS;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static boolean fastTask() throws InterruptedException {
        logger.info("Sleep 5 seconds ...");
        Thread.sleep(5000);
        logger.info("Done sleeping 5 seconds ...");

        return true;
    }

    public static boolean slowTask() throws InterruptedException {
        logger.info("Sleep 10 seconds ...");
        Thread.sleep(10000);
        logger.info("Done sleeping 10 seconds ...");

        return false;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        boolean result = fastSlowTasks();
        logger.info(String.valueOf(result));
    }

    public static boolean fastSlowTasks() throws InterruptedException {

        try (StructuredTaskScope scope = new StructuredTaskScope<Boolean>()) {

            Subtask<Boolean> fastSubtask = scope.fork(() -> fastTask());
            Subtask<Boolean> slowSubtask = scope.fork(() -> slowTask());

            try {
                scope.joinUntil(Instant.now().plusSeconds(7)); // wait for 7 seconds
            } catch (TimeoutException ex) {
                logger.info("Timeout was reached ...");
                scope.shutdown(); // we interrupt the running threads (here, slow task)    
                scope.join();     // avoid IllegalStateException: Owner did not join after forking subtasks
                // since join() is called after shutdown() will return immediately
            }

            logger.info(() -> "Fast subtask state:" + fastSubtask.state());
            logger.info(() -> "Slow subtask state:" + slowSubtask.state());
            
            boolean result = Stream.of(fastSubtask, slowSubtask)
                    .filter(t -> t.state() == SUCCESS) // avoid IllegalStateException: 
                                                       // Result is unavailable or subtask did not complete successfully
                    .collect(Collectors.reducing(true,
                        t -> t.get() , (t1, t2) -> t1 && t2));
                   
            return result;
        }
    }
}
