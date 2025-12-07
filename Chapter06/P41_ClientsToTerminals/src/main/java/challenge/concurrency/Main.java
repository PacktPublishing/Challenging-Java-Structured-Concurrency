package challenge.concurrency;

import java.util.List;
import java.util.concurrent.StructuredTaskScope.FailedException;
import java.util.concurrent.StructuredTaskScope.Joiner;
import static java.util.concurrent.StructuredTaskScope.open;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    record Client(String id, AtomicBoolean connected) {}

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        List<Client> clients = List.of(
                new Client("C1", new AtomicBoolean()),
                new Client("C2", new AtomicBoolean()),
                new Client("C3", new AtomicBoolean()),
                new Client("C4", new AtomicBoolean()),
                new Client("C5", new AtomicBoolean()));

        try {
            connectClients(clients);
        } catch (FailedException e) {
            logger.info("Disconnect clients ...");
            
            for (Client client : clients) {
                client.connected().set(false);
            }
            
            throw e;
        }
    }

    private static void connectClients(List<Client> clients) throws InterruptedException {

        try (var scope = open(Joiner.<Void>allSuccessfulOrThrow())) {

            for (Client client : clients) {
                scope.fork(() -> connectToTerminal(client));
            }

            scope.join();
        }
    }

    private static void connectToTerminal(Client client) {

        try (var scope = open(Joiner.<Void>anySuccessfulOrThrow())) {

            scope.fork(() -> firstTerminal(client));
            scope.fork(() -> secondTerminal(client));
            scope.fork(() -> thirdTerminal(client));

            try { scope.join(); } catch (InterruptedException ex) {}

        } catch (FailedException e) {
            logger.severe(() -> "Client " + client + " cannot be connected to any terminal");
            
            RuntimeException re = new RuntimeException(
                    "Client " + client + " cannot be connected to any terminal");
            re.addSuppressed(e.getCause());
            
            throw re;
        }
    }

    private static void firstTerminal(Client client) {
        
        if (Math.random() < 0.5d) { throw new RuntimeException("Terminal is not available"); }

        if (client.connected().compareAndSet(false, true)) {

            logger.info(() -> "Client " + client
                    + " connected to first terminal by" + Thread.currentThread().toString());
        } else {
            logger.warning(() -> "First terminal: Client " + client + " is already connected");            
        }
    }

    private static void secondTerminal(Client client) {
        
        if (Math.random() < 0.5d) { throw new RuntimeException("Terminal is not available"); }

        if (client.connected().compareAndSet(false, true)) {

            logger.info(() -> "Client " + client
                    + " connected to second terminal by" + Thread.currentThread().toString());
        } else {
            logger.warning(() -> "Second terminal: Client " + client + " is already connected");            
        }
    }

    private static void thirdTerminal(Client client) {
        
        if (Math.random() < 0.5d) { throw new RuntimeException("Terminal is not available"); }

        if (client.connected().compareAndSet(false, true)) {

            logger.info(() -> "Client " + client
                    + " connected to third terminal by" + Thread.currentThread().toString());
        } else {
            logger.warning(() -> "Third terminal: Client " + client + " is already connected");            
        }
    }
}
