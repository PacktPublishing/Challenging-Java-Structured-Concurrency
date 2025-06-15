package challenge.concurrency;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final int NUMBER_OF_PROCESSORS 
            = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws InterruptedException {        
        
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        // This is the task (job) executed by the virtual threads
        Runnable voidTask = () -> logger.info(Thread.currentThread().toString());
                        
        for (int i = 0; i < NUMBER_OF_PROCESSORS + 1; i++) {
            Thread.startVirtualThread(voidTask);
        }
        
        Thread.sleep(5000); // give time to tasks to finish
        
        // reusing workers
        logger.info("");
        for (int i = 0; i < NUMBER_OF_PROCESSORS + 1; i++) {
            Thread.startVirtualThread(voidTask);
            Thread.sleep(1);
        }

        Thread.sleep(5000); // give time to tasks to finish
    }
}
