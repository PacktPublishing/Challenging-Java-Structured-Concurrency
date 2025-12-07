package challenge.concurrency;

import java.util.List;
import java.util.concurrent.StructuredTaskScope.FailedException;
import java.util.concurrent.StructuredTaskScope.Joiner;
import static java.util.concurrent.StructuredTaskScope.open;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    record Connection(String token) {}
    record Client(String id) {}

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        List<Client> clients = List.of(
                new Client("C1"), new Client("C2"), new Client("C3"), new Client("C4"), new Client("C5")
        );
               
        connectClients(clients);        
    }

    private static List<Connection> connectClients(List<Client> clients) throws InterruptedException {

        try (var scope = open(Joiner.<Connection>allSuccessfulOrThrow())) {

            for (Client client : clients) {
                scope.fork(() -> connectToTerminal(client));
            }

            return scope.join();
        }
    }

    private static Connection connectToTerminal(Client client) {

        try (var scope = open(Joiner.<Connection>anySuccessfulOrThrow())) {

            scope.fork(() -> firstTerminal(client));
            scope.fork(() -> secondTerminal(client));
            scope.fork(() -> thirdTerminal(client));

            Connection conn = scope.join();
            
            logger.info(() -> "Client " + client + " can use connection " + conn);
            
            return conn;

        } catch (FailedException e) {
            logger.severe(() -> "Client " + client + " cannot be connected to any terminal");
            
            RuntimeException re = new RuntimeException(
                    "Client " + client + " cannot be connected to any terminal");
            re.addSuppressed(e.getCause());
            
            throw re;
        } catch (InterruptedException ex) {} // if join() is interrupted
        
        return null;
    }

    private static Connection firstTerminal(Client client) {
        
        if (Math.random() < 0.5d) { throw new RuntimeException("Terminal is not available"); }
        
        logger.info(() -> "Connection to first terminal prepared for " + client
                + " by " + Thread.currentThread().toString());
        
        return new Connection("TokenFirstTerminal");
    }

    private static Connection secondTerminal(Client client) {
        
        if (Math.random() < 0.5d) { throw new RuntimeException("Terminal is not available"); }
        
        logger.info(() -> "Connection to second terminal prepared for " + client
                + " by " + Thread.currentThread().toString());
        
        return new Connection("TokenSecondTerminal");
    }
    
    private static Connection thirdTerminal(Client client) {
        
        if (Math.random() < 0.5d) { throw new RuntimeException("Terminal is not available"); }
        
        logger.info(() -> "Connection to third terminal prepared for " + client
                + " by " + Thread.currentThread().toString());
        
        return new Connection("TokenThirdTerminal");
    }
}