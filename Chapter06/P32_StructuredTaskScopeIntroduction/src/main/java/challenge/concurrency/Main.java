package challenge.concurrency;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.StructuredTaskScope.Subtask.State;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
        
        try (StructuredTaskScope scope = new StructuredTaskScope<String>()) {                    

           Subtask<String> roadSubtask = scope.fork(() -> 
                new HighwayServiceCompany("BestRoads").signPartType(HighwaySignPartType.ROAD));        
           Subtask<String> tunnelSubtask = scope.fork(() ->
                new HighwayServiceCompany("TunnelsCo").signPartType(HighwaySignPartType.TUNNEL));
           Subtask<String> bridgeSubtask = scope.fork(() ->
                new HighwayServiceCompany("TheBridges").signPartType(HighwaySignPartType.BRIDGE));                       
        
           scope.join();
           
           HighwayContract contract = new HighwayContract(
                   roadSubtask.state().equals(State.SUCCESS) ? roadSubtask.get() : "not signed",
                   tunnelSubtask.state().equals(State.SUCCESS) ? tunnelSubtask.get() : "not signed", 
                   bridgeSubtask.state().equals(State.SUCCESS) ? bridgeSubtask.get() : "not signed");
        
           logger.info(contract.toString());
        }
    }       
}