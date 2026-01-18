package challenge.concurrency;

public class Main {   

    public static void main(String[] args) {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Thread.ofPlatform().start(() -> new DatabaseServer().startDatabaseServer());
    }
}
