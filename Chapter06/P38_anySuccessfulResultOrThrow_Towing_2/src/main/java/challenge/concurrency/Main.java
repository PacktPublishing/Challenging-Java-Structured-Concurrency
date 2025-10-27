package challenge.concurrency;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.FailedException;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static String bestTowing() throws InterruptedException {
        
        if(Math.random() < 0.8d) { throw new RuntimeException("Best Towing unavailable"); }
        
        Thread.sleep((long) (Math.random() * 1000));
        return "Best Towing is available";
    }
    
    public static String heavyDutyTowing() throws InterruptedException {
        
        if(Math.random() < 0.8d) { throw new RuntimeException("Best Towing unavailable"); }
        
        Thread.sleep((long) (Math.random() * 1000));
        return "Heavy Duty Towing is available";
    }
    
    public static String affordableTowing() throws InterruptedException {
        
        if(Math.random() < 0.8d) { throw new RuntimeException("Best Towing unavailable"); }
        
        Thread.sleep((long) (Math.random() * 1000));
        return "Affordable Towing";
    }
    
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
        
        // StructuredTaskScope<String, String>
        try (var scope = StructuredTaskScope.open(Joiner.<String>anySuccessfulResultOrThrow())) {                    

           Subtask<String> subtask1 = scope.fork(() -> bestTowing());        
           Subtask<String> subtask2 = scope.fork(() -> heavyDutyTowing());
           Subtask<String> subtask3 = scope.fork(() -> affordableTowing());                       
        
            String availableTowing = scope.join();           

           logger.info(() -> "Subtask1 (Best Towing) state: " + subtask1.state());
           logger.info(() -> "Subtask2 (Heavy Duty Towing) state: " + subtask2.state());
           logger.info(() -> "Subtask3 (Affordable Towing) state: " + subtask3.state());                  
           
           logger.info(() -> "Available towing: " + availableTowing);
        } catch (FailedException e) {
            
            logger.severe(e.toString());
            
            throw new TowingException("Sorry, no towing service is available in your area");
        }
    }       
}