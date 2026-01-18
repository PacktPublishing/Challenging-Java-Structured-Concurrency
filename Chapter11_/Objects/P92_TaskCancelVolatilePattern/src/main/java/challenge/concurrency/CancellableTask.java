package challenge.concurrency;

import java.util.logging.Logger;

public class CancellableTask implements Runnable {

    private static final Logger logger = Logger.getLogger(CancellableTask.class.getName());

    private volatile boolean cancelled;

    @Override
    public void run() {

        while (!cancelled) {
            logger.info(() -> "Task running ... " + Thread.currentThread().toString());
            // task code
        }
    }

    public boolean isAlive() {

        return Thread.currentThread().isAlive();
    }

    public boolean isInterrupted() {

        return Thread.currentThread().isInterrupted();
    }

    public void cancel() {

        cancelled = true;
    }
}
