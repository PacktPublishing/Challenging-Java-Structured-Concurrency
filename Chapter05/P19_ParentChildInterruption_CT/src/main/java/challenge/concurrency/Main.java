package challenge.concurrency;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Thread childThread = new Thread(() -> {
            try {
                logger.info("Child thread is working...");
                Thread.sleep(3000); // Simulate child thread working
            } catch (InterruptedException e) {
                logger.severe("Child thread was interrupted. Preparing the response/result/cleanup/ ...");
            }
            logger.info("Child thread finished work.");
        });

        childThread.start();

        Thread.sleep(1500);      // Let the child thread start its work
        childThread.interrupt(); // Interrupt the child thread

        // Wait for the child thread to finish 
        // If it was interrupted, as here, wait for its catch block to be executed
        childThread.join();

        logger.info("Parent thread can go forward.");
    }
}
