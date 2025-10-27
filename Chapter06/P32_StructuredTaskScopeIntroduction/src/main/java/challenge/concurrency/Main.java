package challenge.concurrency;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
                
        // StructuredTaskScope<Contract, Void>
        try (var scope = StructuredTaskScope.open()) {                    

           Subtask<RoadContract> roadSubtask = scope.fork(() -> 
                (RoadContract) new HighwayServiceCompany("BestRoads")
                        .signPartType(HighwaySignPartType.ROAD));        
           Subtask<TunnelContract> tunnelSubtask = scope.fork(() ->
                (TunnelContract) new HighwayServiceCompany("TunnelsCo")
                        .signPartType(HighwaySignPartType.TUNNEL));
           Subtask<BridgeContract> bridgeSubtask = scope.fork(() ->
                (BridgeContract) new HighwayServiceCompany("TheBridges")
                        .signPartType(HighwaySignPartType.BRIDGE));                       
                 
           scope.join(); // Join subtasks, propagating exceptions
           
           // All subtasks have succeeded, so compose their results
           HighwayContract contract = new HighwayContract(
                   roadSubtask.get(), tunnelSubtask.get(), bridgeSubtask.get());
        
           logger.info(contract.toString());
        }
    }       
}