package challenge.concurrency;

import java.lang.ScopedValue.Carrier;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static final ScopedValue<String> SV = ScopedValue.newInstance();
    public static final Carrier carrier = ScopedValue.where(SV, "Anonymous");

    public static void login() {
        logger.info(SV.get());
        ScopedValue.where(SV, "user"); // this returns another Carrier
        logger.info(SV.get());
        
        Thread.ofVirtual().start(() -> carrier.run(() -> User.user()));
    }

    public static void logout() {
        Thread.ofVirtual().start(() -> carrier.run(() -> logger.info(SV.get())));
        User.user();
    }

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        ScopedValue.where(SV, "Anonymous").run(() -> login());
        carrier.run(() -> logout());
        carrier.run(() -> User.user());

        Thread.ofVirtual().start(() -> { // Thread.ofPlatform()
            carrier.run(() -> login());
            carrier.run(() -> logout());
            carrier.run(() -> User.user());
        }).join();
    }
}