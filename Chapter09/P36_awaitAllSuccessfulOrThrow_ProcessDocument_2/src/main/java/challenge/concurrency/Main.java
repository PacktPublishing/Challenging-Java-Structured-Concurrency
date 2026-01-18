package challenge.concurrency;

import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.FailedException;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Summary summary = processDocument();

        logger.info(summary.toString());
    }

    public static Summary processDocument() throws InterruptedException {

        // StructuredTaskScope<Object, Void>        
        try (var scope = StructuredTaskScope.open()) {

            Subtask<Validator> vSubtask = scope.fork(() -> ValidatorService.validate());
            Subtask<Boolean> fSubtask = scope.fork(() -> FormatterService.format());
            Subtask<List<Spelling>> sSubtask = scope.fork(() -> SpellingService.spellingCheck());
            
            scope.join(); // Join subtasks, propagating exceptions

            // All subtasks have succeeded, so compose their results
            Summary summary = new Summary(fSubtask.get(), sSubtask.get(), vSubtask.get());

            return summary;
        } catch (FailedException ex) {

            Throwable cause = ex.getCause();
            logger.severe(cause.toString());
            
            throw new ServiceException("Sorry, we cannot process your document");
        }
    }
}
