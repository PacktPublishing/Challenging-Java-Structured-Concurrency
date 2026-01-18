package challenge.concurrency;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable cleanupTask = () -> {

            logger.info("Log cleaner in action ...");

            try { Thread.sleep(1000); } catch (InterruptedException ex) {}
        };

        LogFutureTask ft = new LogFutureTask(cleanupTask, "Cleanup complete");

        while (Math.random() > 0.2d) {
            ft.runAndReset();
        }

        ft.run(); // Complete the task

        String result = (String) ft.get();
        logger.info(result);
    }
}
