package challenge.concurrency;

import java.time.Duration;
import java.util.concurrent.StructuredTaskScope;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    private static final int TIMEOUT = 1100;

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
        
        Operator operator = call();
        logger.info(() -> "You're on the phone with " + operator);
    }
   
    public static Operator call() throws InterruptedException {

        try (var scope = StructuredTaskScope.open(new PhoneLineScope(), 
                cf -> cf.withTimeout(Duration.ofMillis(TIMEOUT)))) {

            scope.fork(() -> phoneOperator1());
            scope.fork(() -> phoneOperator2());
            scope.fork(() -> phoneOperator3());

            return scope.join();
        } 
    }
    
    private static PhoneOperator phoneOperator1() throws InterruptedException {
        
        Thread.sleep((long) (Math.random() * 5000));
        
        return new PhoneOperator("PO1");
    }
    
    private static PhoneOperator phoneOperator2() throws InterruptedException {
        
        Thread.sleep((long) (Math.random() * 5000));
        
        return new PhoneOperator("PO2");
    }
    
    private static PhoneOperator phoneOperator3() throws InterruptedException {
        
        Thread.sleep((long) (Math.random() * 5000));
        
        return new PhoneOperator("PO3");
    }        
}
