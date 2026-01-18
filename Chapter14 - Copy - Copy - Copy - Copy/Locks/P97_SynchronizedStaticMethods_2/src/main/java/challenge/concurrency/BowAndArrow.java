package challenge.concurrency;

import java.util.logging.Logger;

public class BowAndArrow {

    private static final Logger logger = Logger.getLogger(BowAndArrow.class.getName());

    public static int arrows = 10;

    public synchronized static void shoot() {

        logger.info(() -> "Shot by " + Thread.currentThread().getName());
        arrows--;       
    }

    public synchronized static void load() {

        logger.info(() -> "Load by " + Thread.currentThread().getName());
        arrows++;
    }
}
