package challenge.concurrency;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope.ShutdownOnFailure;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());  
    
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
        
        try (ShutdownOnFailure scope = new ShutdownOnFailure()) {                    

           Subtask<String> roadSubtask = scope.fork(() -> 
                new HighwayServiceCompany("BestRoads").signPartType(HighwaySignPartType.ROAD));        
           Subtask<String> tunnelSubtask = scope.fork(() ->
                new HighwayServiceCompany("TunnelsCo").signPartType(HighwaySignPartType.TUNNEL));
           Subtask<String> bridgeSubtask = scope.fork(() ->
                new HighwayServiceCompany("TheBridges").signPartType(HighwaySignPartType.BRIDGE));                       
        
           scope.join();   
           
            Optional<Throwable> exception = scope.exception();
            if (exception.isEmpty()) {
               logger.info(() -> "Subtask-road state: " + roadSubtask.state());
               logger.info(() -> "Subtask-tunnels state: " + tunnelSubtask.state());
               logger.info(() -> "Subtask-bridges state: " + bridgeSubtask.state());
                                    
                HighwayContract contract = new HighwayContract(
                   roadSubtask.get(), tunnelSubtask.get(), bridgeSubtask.get());
           
               logger.info(contract.toString());
            } else {
                logger.info(() -> exception.get().getMessage());
                scope.throwIfFailed();
            }
        }
    }       
}