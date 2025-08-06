package challenge.concurrency;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static boolean fastTask() throws InterruptedException {
        logger.info("Sleep 5 seconds ...");
        Thread.sleep(5000);
        logger.info("Done sleeping 5 seconds ...");
        
        return true;
    }

    public static boolean slowTask() throws InterruptedException {
        logger.info("Sleep 10 seconds ...");
        Thread.sleep(10000);
        logger.info("Done sleeping 10 seconds ...");
        
        return false;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        try (StructuredTaskScope scope = new StructuredTaskScope<Boolean>()) {
                    
            Subtask<Boolean> fastSubtask = scope.fork(() -> fastTask());
            Subtask<Boolean> slowSubtask = scope.fork(() -> slowTask());                 
                                         
            scope.shutdown(); // Shut down this task scope without closing it
            
            scope.join();     // returns immediately
                        
            logger.info(() -> "Fast subtask state:" + fastSubtask.state());
            logger.info(() -> "Slow subtask state:" + slowSubtask.state());            
        }
    }    
}