package challenge.concurrency;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.FailedException;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.StructuredTaskScope.Subtask.State;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws Throwable {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
                
        Subtask<RoadContract> roadSubtask = null;
        Subtask<TunnelContract> tunnelSubtask = null;
        Subtask<BridgeContract> bridgeSubtask = null;
                
        // StructuredTaskScope<Contract, Void>
        try (var scope = StructuredTaskScope.open()) {                    

           roadSubtask = scope.fork(() -> 
                (RoadContract) new HighwayServiceCompany("BestRoads")
                        .signPartType(HighwaySignPartType.ROAD));        
           tunnelSubtask = scope.fork(() ->
                (TunnelContract) new HighwayServiceCompany("TunnelsCo")
                        .signPartType(HighwaySignPartType.TUNNEL));
           bridgeSubtask = scope.fork(() ->
                (BridgeContract) new HighwayServiceCompany("TheBridges")
                        .signPartType(HighwaySignPartType.BRIDGE));                       
                 
           scope.join(); // Join subtasks, propagating exceptions
           
           // All subtasks have succeeded, so compose their results
           HighwayContract contract = new HighwayContract(
                   roadSubtask.get(), tunnelSubtask.get(), bridgeSubtask.get());
        
           logger.info(contract.toString());
        } catch (FailedException e) {
            
            State roadSubtaskState = roadSubtask.state();
            State tunnelSubtaskState = tunnelSubtask.state();
            State bridgeSubtaskState = bridgeSubtask.state();
                       
            logger.info(() -> "Road subtask state: " + roadSubtaskState);
            logger.info(() -> "Tunnel subtask state: " + tunnelSubtaskState);
            logger.info(() -> "Bridge subtask state: " + bridgeSubtaskState);
            
            throw e.getCause();
        }
    }       
}