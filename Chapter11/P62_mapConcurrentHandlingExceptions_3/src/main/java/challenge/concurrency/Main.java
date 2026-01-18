package challenge.concurrency;

import java.util.List;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Gatherers.mapConcurrent;
import java.util.stream.Stream;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static class Terminal {

        public static String connect(String service) throws ConnectionException {

            logger.info(() -> "Service " + service + " (" + Thread.currentThread() + ") "
                    + "attempts to acquire a connection ...");

            if (Math.random() < 0.2d) {
                throw new ConnectionException(
                        ConnectionStatus.CONNECTION_EXHAUSTED); // unrecoverable exception               
            }

            if (Math.random() < 0.6d) {
                throw new ConnectionException(
                        ConnectionStatus.CONNECTION_TIMEOUT); // recoverable exception               
            }

            return service + " connected";
        }
        
        public static String restart() { // should be done only when no services tries to connect
            logger.warning("Restarting terminal ...");
            
            return "restarted";
        }
    }

    public static void main(String[] args) {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        List<String> services = Stream.of("S1", "S2", "S3", "S4", "S5", "S6")
                .gather(mapConcurrent(3, service -> {
                    try {
                        return Terminal.connect(service);
                    } catch (ConnectionException ex) {
                        return switch (ex.getStatus()) {
                            case CONNECTION_TIMEOUT -> service + " timeout";
                            default -> Terminal.restart(); // don't do this since other services try to connect
                        };
                    }
                })).collect(toList());

        logger.info(() -> "Connected services: " + services);
    }
}
