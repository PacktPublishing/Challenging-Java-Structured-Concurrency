package challenge.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    public static void main(String[] args) {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        CompletableFuture<String> cf = new CompletableFuture();

        cf.complete("Completed ...");
        
        // logger.info(cf.getNow("Not completed, default result ..."));                        
        logger.info(cf.resultNow());                        
    }
}
