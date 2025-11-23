package challenge.concurrency;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
       
        ResultTracker rt = new ResultTracker();
        logger.info(() -> "Initial state: " + rt.getResult().toString());
        
        rt.newResult(2, 1000);
        logger.info(() -> "New state: " + rt.getResult().toString());
        
    }
}
