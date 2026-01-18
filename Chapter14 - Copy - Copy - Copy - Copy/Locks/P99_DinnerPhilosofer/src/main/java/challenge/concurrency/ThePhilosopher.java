package challenge.concurrency;

import java.util.logging.Logger;

public class ThePhilosopher implements Runnable {

    private static final Logger logger = Logger.getLogger(ThePhilosopher.class.getName());    

    private final String forkFromLeft;
    private final String forkFromRight;

    public ThePhilosopher(String forkFromLeft, String forkFromRight) {
        this.forkFromLeft = forkFromLeft;
        this.forkFromRight = forkFromRight;
    }

    @Override
    public void run() {

        while (true) {
            logger.info(() -> Thread.currentThread().getName() + ": thinking");
            action();
            synchronized (forkFromLeft) {
                logger.info(() -> Thread.currentThread().getName()
                        + ": took the left fork (" + forkFromLeft + ")");
                action();
                synchronized (forkFromRight) {
                    logger.info(() -> Thread.currentThread().getName()
                            + ": took the right fork (" + forkFromRight + ") and eating");
                    action();
                    logger.info(() -> Thread.currentThread().getName()
                            + ": put the right fork ( " + forkFromRight + ") on the table");
                    action();
                }
                logger.info(() -> Thread.currentThread().getName()
                        + ": put the left fork (" + forkFromLeft + ") on the table and thinking");
                action();
            }
        }
    }

    private static void action() {
        try {
            Thread.sleep((long) (Math.random() * 2000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.severe(() -> "Ops!: " + e);
        }
    }
}