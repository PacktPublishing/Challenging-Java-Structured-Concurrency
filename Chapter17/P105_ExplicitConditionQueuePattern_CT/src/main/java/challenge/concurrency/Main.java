package challenge.concurrency;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final int MAX_MISSPELLINGS = 10;
    private int misspellingsCount = 0;
  
    public synchronized void stopSpellingCheck() throws InterruptedException {

        while (misspellingsCount < MAX_MISSPELLINGS) {
            wait();
        }

        logger.info("You reached maximum misspellings. Go premium or wait for 10s to continue ...");
        Thread.sleep(10_000);

        misspellingsCount = 0;
        notify();
    }

    public synchronized void spellingCheck(String textToCheck) throws InterruptedException {

        while (misspellingsCount >= MAX_MISSPELLINGS) {
            notify();
            wait();
        }

        Thread.sleep((long) (Math.random() * 100)); // checking text

        int misspellings = 1 + (int) (Math.random() * 5);
        logger.info(() -> "Found " + misspellings + " misspellings");

        misspellingsCount += misspellings;
    }

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Main spellingQueue = new Main();

        Thread t1 = Thread.ofVirtual().start(() -> {

            while (true) {
                try {
                    spellingQueue.spellingCheck("here we need the text to check");
                } catch (InterruptedException e) {
                }
            }
        });

        Thread t2 = Thread.ofVirtual().start(() -> {

            while (true) {
                try {
                    spellingQueue.stopSpellingCheck();
                } catch (InterruptedException e) {
                }
            }
        });

        t1.join();
        t2.join();
    }
}
