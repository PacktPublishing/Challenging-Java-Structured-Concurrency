package challenge.concurrency;

import static java.lang.Thread.sleep;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
        
       CompletableFuture<Boolean> fastTask = CompletableFuture.supplyAsync(() -> {
            try { sleep(5000); } catch (InterruptedException ex) {}
            return true;
        });
       
       CompletableFuture<Boolean> slowTask = CompletableFuture.supplyAsync(() -> {
            try { sleep(10000); } catch (InterruptedException ex) {}
            return false;
        });
       
       CompletableFuture<Void> shutdownTask = CompletableFuture.runAsync(() -> {
            try { sleep(7000); } catch (InterruptedException ex) {}
            
            fastTask.cancel(true);
            slowTask.cancel(true);           
        });
       
       shutdownTask.get();
               
       if(shutdownTask.isDone()){
           logger.info(() -> "Fast task was cancelled ? " + fastTask.isCancelled());
           logger.info(() -> "Slow task was cancelled ? " + slowTask.isCancelled());
            
           boolean fastTaskResult = fastTask.get();
           logger.info(() -> "Retrieved result from the fast task: " +  fastTaskResult);           
        }      
    }
}