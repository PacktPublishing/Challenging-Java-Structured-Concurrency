package challenge.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final int MAX_NR_OF_THREADS = 10;

    static class VtPtThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            // return Thread.ofPlatform().unstarted(r); // platform thread
            return Thread.ofVirtual().unstarted(r); // virtual thread
        }
    }

    public static void main(String[] args) {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        try (ExecutorService vtptExecutor = Executors
                .newThreadPerTaskExecutor(new VtPtThreadFactory())) {

            for (int i = 0; i < MAX_NR_OF_THREADS; i++) {
                int nr = i;                       
                vtptExecutor.submit(() -> taskNr(nr));
            }
        }
    }

    public static void taskNr(int nr) {

        logger.info(() -> Thread.currentThread().toString() + " " + nr);
        try { 
            Thread.sleep(3000);                
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt(); // log "ex"
        }
        logger.info(() -> Thread.currentThread().toString() + " " + nr);
    }
}