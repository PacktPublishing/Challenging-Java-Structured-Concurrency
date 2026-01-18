package challenge.concurrency;

import java.util.List;
import java.util.concurrent.StructuredTaskScope.Joiner;
import static java.util.concurrent.StructuredTaskScope.open;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        String ipv4 = ipv4Generator();

        logger.info(ipv4);
    }

    public static String ipv4Generator() throws InterruptedException {
        
        // StructuredTaskScope<Integer,  List<Integer>>
        try (var scope = open(Joiner.<Integer>allSuccessfulOrThrow())) {

            scope.fork(() -> ipv4Octet1());
            scope.fork(() -> ipv4Octet2());
            scope.fork(() -> ipv4Octet3());
            scope.fork(() -> ipv4Octet4());

            List<Integer> results = scope.join(); // Join subtasks, propagating exceptions

            // All subtasks have succeeded, so compose their results
            String ipv4Result = results.stream()
                    .collect(mapping(String::valueOf, joining(".")));

            return ipv4Result;
        }
    }
    
    public static int ipv4Octet1() {

        return ThreadLocalRandom.current().nextInt(0, 193);
    }
    
    public static int ipv4Octet2() {

        return ThreadLocalRandom.current().nextInt(0, 169);
    }
    
    public static int ipv4Octet3() {

        return ThreadLocalRandom.current().nextInt(0, 255);
    }
    
    public static int ipv4Octet4() {

        return ThreadLocalRandom.current().nextInt(110, 130);
    }
}
