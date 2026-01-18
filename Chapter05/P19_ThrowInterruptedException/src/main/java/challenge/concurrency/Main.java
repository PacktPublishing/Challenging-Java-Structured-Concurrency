package challenge.concurrency;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void task() {
        try {
            logger.info("Thread is going to sleep.");
            Thread.sleep(3000);  // Sleep for 3 seconds
            logger.info("Thread woke up normally.");
        } catch (InterruptedException e) {
            logger.info("Thread was interrupted while sleeping.");
        }
    }
    
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Thread thread = Thread.ofVirtual().name("vt").start(() -> task());        

        Thread.sleep(1000);  // Main thread sleeps for 1 seconds
        thread.interrupt();  // Interrupt the sleeping thread
        thread.join();
    }
}
