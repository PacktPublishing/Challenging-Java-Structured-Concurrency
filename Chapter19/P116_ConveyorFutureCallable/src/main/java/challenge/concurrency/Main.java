package challenge.concurrency;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Conveyor.on();
        Thread.sleep(20000); // run for 20 seconds
        Conveyor.off();       
    }
}
