package challenge.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws TimeoutException, InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
            
        CompletableFuture<String> roadsCf = CompletableFuture.supplyAsync(() -> {
            return new HighwayServiceCompany("BestRoads").signPartType(HighwaySignPartType.ROAD);
        });
        
        CompletableFuture<String> tunnelsCf = CompletableFuture.supplyAsync(() -> {
            return new HighwayServiceCompany("TunnelsCo").signPartType(HighwaySignPartType.TUNNEL);
        });
        
        CompletableFuture<String> bridgesCf = CompletableFuture.supplyAsync(() -> {
            return new HighwayServiceCompany("TheBridges").signPartType(HighwaySignPartType.BRIDGE);
        });
                
        CompletableFuture.allOf(roadsCf, tunnelsCf, bridgesCf).get(3, TimeUnit.SECONDS);
        
        HighwayContract contract = new HighwayContract(
                roadsCf.resultNow(), tunnelsCf.resultNow(), bridgesCf.resultNow());
        
        logger.info(contract.toString());        
    }             
}