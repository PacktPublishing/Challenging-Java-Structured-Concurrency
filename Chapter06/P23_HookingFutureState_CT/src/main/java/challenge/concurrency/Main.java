package challenge.concurrency;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        String courier = findCourier();
        logger.info(courier);
    }

    public static String findCourier() {

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            
            String courier = executor.invokeAny(
                    List.of(() -> fanCourier(),
                            () -> cargusCourier(),
                            () -> somedayCourier()),
                    3, TimeUnit.SECONDS
            );
            
            return courier;
            
        } catch (InterruptedException | TimeoutException ex) {
            return "No courier responded within the allotted time";
        } catch (ExecutionException ex) {
            return "No couriers are available";
        }
    }
    
    public static String fanCourier() {
        // return "Fan-Courier";
        throw new RuntimeException("Closed");
    }
    
    public static String cargusCourier() throws InterruptedException {
        // Thread.sleep(5000);
        // return "Cargus-Courier";
        throw new RuntimeException("Closed");
    }
    
    public static String somedayCourier() {
        throw new RuntimeException("Closed");        
    }
}
