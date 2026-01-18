package challenge.concurrency;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());   
    private static final int NUMBER = 15;

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
              
        long f = RecursiveScopedFibonacci.execute(NUMBER);
        
        logger.info(() -> "Fibonacci (" + NUMBER + "): " + f);
    }
}
