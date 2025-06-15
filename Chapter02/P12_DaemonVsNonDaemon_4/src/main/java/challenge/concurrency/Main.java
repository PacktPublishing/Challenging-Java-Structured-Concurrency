package challenge.concurrency;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Thread.UncaughtExceptionHandler injuryExceptionHandler = (Thread t, Throwable e) -> {

            t.interrupt();                       
            
            logger.severe(() -> "!!! Injury of " + t.getName());                       
            logger.severe(() -> "Call the doctor for " + t.getName());
        };

        // This is the task (job) executed by the daemon thread (clock thread)
        Runnable clockTask = () -> {
            while (true) {
                logger.info(ZonedDateTime.now()
                        .format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                try {
                    Thread.sleep(Duration.ofSeconds(1));
                } catch (InterruptedException ex) {
                }
            }
        };

        // This is the task (job) executed by the non-daemon threads (powerlifter's threads)
        Runnable deadliftTask = () -> {

            long restBreak = 0;
            while (restBreak < 15_000) { // 15 seconds              
                logger.info(() -> "Deadlift of " + Thread.currentThread().getName());

                // randomly simulate an injury - we must stop the event
                if (Math.random() < 0.2d) {
                    throw new InjuryException("Injury");
                }
                try {
                    restBreak = (long) (Math.random() * 20_000); // the powerlifter looks at the clock                      
                    Thread.sleep(restBreak);
                } catch (InterruptedException ex) {
                }
            }
        };

        // the clock thread is a virtual thread (daemon thread)     
        Thread clockThread = Thread.ofVirtual().unstarted(clockTask);

        // powerlifter's threads
        Thread powerlifterThread1 = Thread.ofPlatform()
                .name("Powerlifter-1")
                .uncaughtExceptionHandler(injuryExceptionHandler)
                .unstarted(deadliftTask);
        Thread powerlifterThread2 = Thread.ofPlatform()
                .name("Powerlifter-2")
                .uncaughtExceptionHandler(injuryExceptionHandler)
                .unstarted(deadliftTask);

        logger.info("Start deadlift contest ...");

        clockThread.start();

        powerlifterThread1.start();
        powerlifterThread2.start();
    }
}
