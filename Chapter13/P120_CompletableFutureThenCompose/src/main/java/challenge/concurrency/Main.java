package challenge.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    public record Server (String token) {} 
    public static class ServerService {
        
        public static Server server(String token) { return new Server(token); }
    }
    
    public record Adapter (Server server) {} 
    public static class AdapterService {
        
        public static Adapter adapter(Server server) { return new Adapter(server); }
    }
    
    public static CompletableFuture<Server> getServerCf(String token) {
        return CompletableFuture.supplyAsync(() -> ServerService.server(token));
    }
    
    public static CompletableFuture<Adapter> getAdapterCf(Server server) {
        return CompletableFuture.supplyAsync(() -> AdapterService.adapter(server));
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

       Adapter adapter1 = getServerCf("ServerToken")
               .thenApply(server -> getAdapterCf(server)).get().get();
       logger.info(() -> "Adapter: " + adapter1);
       
       // better use thenCompose
       Adapter adapter2 = getServerCf("ServerToken")
               .thenCompose(server -> getAdapterCf(server)).get();
       logger.info(() -> "Adapter: " + adapter2);
    }
}
