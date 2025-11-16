package challenge.concurrency;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final ThreadLocal<String> tl = new ThreadLocal<>();
    private static final InheritableThreadLocal<String> itl = new InheritableThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Thread.ofVirtual().name("Parent-Thread").start(() -> {

            tl.set(Thread.currentThread().getName());
            itl.set(Thread.currentThread().getName());

            logger.info(() -> Thread.currentThread() + ": " + tl.get());
            logger.info(() -> Thread.currentThread() + ": " + itl.get());

            try {
                Thread.ofVirtual().name("Child-Thread").start(() -> {
                    logger.info(() -> Thread.currentThread() + ": " + tl.get());
                    logger.info(() -> Thread.currentThread() + ": " + itl.get());

                    tl.set(Thread.currentThread().getName());
                    itl.set(Thread.currentThread().getName());
                    
                    logger.info(() -> Thread.currentThread() + ": " + tl.get());
                    logger.info(() -> Thread.currentThread() + ": " + itl.get());
                }).join();
            } catch (InterruptedException ex) {}

            logger.info(() -> Thread.currentThread() + ": " + tl.get());
            logger.info(() -> Thread.currentThread() + ": " + itl.get());
        }).join();
    }
}
