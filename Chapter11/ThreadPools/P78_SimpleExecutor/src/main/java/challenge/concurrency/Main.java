package challenge.concurrency;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());    

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable voidTask = ()
                -> logger.info(() -> "Runnable:" + Thread.currentThread().toString());

        SimpleExecutor executor = new SimpleExecutor();
        
        executor.execute(voidTask);
        executor.executeVirtualThread(voidTask).join();
    }
}