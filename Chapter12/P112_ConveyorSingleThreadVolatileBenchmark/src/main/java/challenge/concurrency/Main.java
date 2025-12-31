package challenge.concurrency;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        // Warmup
        Conveyor.on();
        Thread.sleep(5000);
        Conveyor.off();

        // Let things (like JIT compilations) calm down
        Thread.sleep(2000);

        System.out.println("\nStarting the conveyor again ...");

        // Test
        Conveyor.on();
        Thread.sleep(15000);
        Conveyor.off();
        
        Conveyor.outputLatencyHistogram();
    }
}
