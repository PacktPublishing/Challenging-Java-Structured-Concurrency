package challenge.concurrency;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
        
        try (ExecutorService executor = Executors.newFixedThreadPool(3)) {                    

           Future<String> roadFuture = executor.submit(() -> 
                new HighwayServiceCompany("BestRoads").signPartType(HighwaySignPartType.ROAD));        
           Future<String> tunnelFuture = executor.submit(() ->
                new HighwayServiceCompany("TunnelsCo").signPartType(HighwaySignPartType.TUNNEL));
           Future<String> bridgeFuture = executor.submit(() ->
                new HighwayServiceCompany("TheBridges").signPartType(HighwaySignPartType.BRIDGE));                       
        
           HighwayContract contract = new HighwayContract(roadFuture.get(), 
           tunnelFuture.get(), bridgeFuture.get());
        
           logger.info(contract.toString());
        }
    }       
}