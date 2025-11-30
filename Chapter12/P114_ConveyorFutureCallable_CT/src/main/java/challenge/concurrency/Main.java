package challenge.concurrency;

import java.time.Duration;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Conveyor.on();
        Thread.sleep(Duration.ofMinutes(2));
        Conveyor.off();       
    }
}
