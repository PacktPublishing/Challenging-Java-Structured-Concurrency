package challenge.concurrency;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import static java.util.concurrent.StructuredTaskScope.open;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final StructuredTaskScope scope = open(Joiner.<String>awaitAll());     

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        try {
            int i = 0;
            while (Math.random() < 0.95d) { // simulate a random number of clients
                forkDownload("file_" + i);
                i++;
            }

            scope.join();
        } finally {
            closeScope();
        }
    }

    public static void forkDownload(String file) {

        scope.fork(() -> download(file));
    }

    public static void closeScope() {
        
        logger.info("Closing scope ...");
        scope.close();        
    }

    public static void download(String file) {

        try { Thread.sleep((long) (Math.random() * 500)); } catch (InterruptedException ex) {}

        if (Math.random() < 0.3d) { throw new RuntimeException("Download exception (" + file + ")"); }

        logger.info(() -> "Downloaded " + file + " by " + Thread.currentThread().toString());
    }
}
