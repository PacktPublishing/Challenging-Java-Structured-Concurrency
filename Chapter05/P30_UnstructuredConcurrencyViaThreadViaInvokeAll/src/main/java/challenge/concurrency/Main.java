package challenge.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
        
        try (ExecutorService executor = Executors.newFixedThreadPool(3)) {                            
                    
           List<String> contracts = new ArrayList<>();
            
           List<Future<String>> contractFutures = executor.invokeAll(
                    List.of(() -> new HighwayServiceCompany("BestRoads").signPartType(HighwaySignPartType.ROAD),
                            () -> new HighwayServiceCompany("TunnelsCo").signPartType(HighwaySignPartType.TUNNEL),
                            () -> new HighwayServiceCompany("TheBridges").signPartType(HighwaySignPartType.BRIDGE)), 
                    3, TimeUnit.SECONDS
           );         
           
           contractFutures.forEach(f -> {
               switch (f.state()) {                    
                    case SUCCESS -> contracts.add(f.resultNow());                    
                    case FAILED, CANCELLED, RUNNING -> contracts.add("not signed");                    
               }
           });
        
           HighwayContract contract = new HighwayContract(
                   contracts.get(0), contracts.get(1), contracts.get(2));
        
           logger.info(contract.toString());
        }
    }       
}