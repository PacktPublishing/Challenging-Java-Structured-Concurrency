package challenge.concurrency;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static class InitNetwork implements Runnable {

        @Override
        public void run() {
            try {
                // Connecting to network
                logger.info("Initialize network connection ...");
                    
                while (true) {                                        
                    // Connection success
                    logger.info("Transferring data ...");
                    
                    Thread.sleep(1000);  // Simulate network delay
                }
            } catch (InterruptedException e) {
                logger.warning("Data transfer interrupted. Cleaning up ...");
                // Perform clean-up tasks (e.g., closing network connections)
            } finally {
                logger.warning("Network app stopped.");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Thread thread = Thread.ofVirtual().name("vt").start(new InitNetwork());

        Thread.sleep(5000);  // Run for 5 seconds
        logger.info("Interrupting network thread ...");
        thread.interrupt();  // Interrupt the network task
        thread.join();
    }
}
