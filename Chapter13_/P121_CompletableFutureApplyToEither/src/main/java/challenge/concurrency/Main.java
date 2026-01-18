package challenge.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
  
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
       
        record WiFiConnection(String name) {}
        
        CompletableFuture<WiFiConnection> wiFiCf1 = CompletableFuture.supplyAsync(() -> {

            try { Thread.sleep((long) (Math.random() * 10000)); } catch (InterruptedException ex) {}

            return new WiFiConnection("WiFi-1");
        });
        
        CompletableFuture<WiFiConnection> wiFiCf2 = CompletableFuture.supplyAsync(() -> {

            try { Thread.sleep((long) (Math.random() * 10000)); } catch (InterruptedException ex) {}

            return new WiFiConnection("WiFi-2");
        });
        
        Function<WiFiConnection, String> connectServer = wificon -> {                        
            
            return "Server connected to " + wificon;
        };
        
         CompletableFuture<String> serverCf =  wiFiCf1.applyToEither(wiFiCf2, connectServer);
         
         String result = serverCf.get();
         
         logger.info(result);
    }
}
