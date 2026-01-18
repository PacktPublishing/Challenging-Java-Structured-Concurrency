package challenge.concurrency;

import java.time.Duration;
import java.util.logging.Logger;

public class DatabaseProcess {

    private static final Logger logger = Logger.getLogger(DatabaseProcess.class.getName());

    private final String process;

    public DatabaseProcess(String process) {
        this.process = process;
    }

    public void startProcess() {

        int startInAround = (int) (Math.random() * 10);

        try {
            logger.info(() -> "Starting database process: '" + process + "'");
            Thread.sleep(Duration.ofSeconds(startInAround));

            logger.info(() -> "Database process '"
                    + process + "' started in " + startInAround + " seconds");            

        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
