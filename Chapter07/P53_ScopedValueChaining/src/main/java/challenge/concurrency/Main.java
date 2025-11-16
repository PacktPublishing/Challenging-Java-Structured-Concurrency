package challenge.concurrency;

import java.io.File;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final ScopedValue<String> SVS = ScopedValue.newInstance();
    private static final ScopedValue<Integer> SVI = ScopedValue.newInstance();
    private static final ScopedValue<File> SVF = ScopedValue.newInstance();

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable runnableTask = () -> {
            logger.info(() -> Thread.currentThread().toString()
                    + " | " + (SVS.isBound() ? SVS.get() : "Not bound")
                    + " | " + (SVI.isBound() ? SVI.get() : "Not bound")
                    + " | " + (SVF.isBound() ? SVF.get() : "Not bound"));

        };

        Thread vt = Thread.ofVirtual().start(() -> {
            ScopedValue.where(SVS, "Bumfuzzle")
                    .where(SVI, 1)
                    .where(SVF, new File("bumfuzzle.txt"))
                    .run(runnableTask);
        });

        vt.join();
    }
}
