package challenge.concurrency;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        CancellableTask task = new CancellableTask();
        task.run(true);

        logger.info(() -> "Task is alive ? " + task.isAlive());
        logger.info(() -> "Task is interrupted ? " + task.isInterrupted());

        Thread.sleep(10); // give some time to the task to run
        
        task.cancel();

        Thread.sleep(10); // give some time to the task to be cancelled
        
        logger.info(() -> "Task is alive ? " + task.isAlive());
        logger.info(() -> "Task is interrupted ? " + task.isInterrupted());
    }
}
