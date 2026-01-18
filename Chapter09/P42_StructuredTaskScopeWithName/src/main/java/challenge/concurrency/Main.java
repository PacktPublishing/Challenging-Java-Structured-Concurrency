package challenge.concurrency;

import java.util.concurrent.StructuredTaskScope.Joiner;
import static java.util.concurrent.StructuredTaskScope.open;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        try (var scope = open(Joiner.<Void>allSuccessfulOrThrow(),
                cf -> cf.withName("outerScope"))) {

            // scope subtasks
            
            logger.info(() -> "Scope " + scope.toString() 
                    + " is cancelled ? " + scope.isCancelled());
        }

        try (var scope = open(Joiner.<Void>anySuccessfulOrThrow(),
                cf -> cf.withName("innerScope"))) {
            
            // scope subtasks
            
            logger.info(() -> "Scope " + scope.toString() 
                    + " is cancelled ? " + scope.isCancelled());
        }
    }
}
