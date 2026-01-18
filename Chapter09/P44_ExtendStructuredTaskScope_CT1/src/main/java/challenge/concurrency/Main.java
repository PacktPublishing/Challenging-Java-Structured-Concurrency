package challenge.concurrency;

import java.util.Arrays;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.FailedException;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        String clientLocation = "123 East 55st Street"; // shared by the client

        try {
            PrivateECS privateEcs = findPrivateECS(clientLocation);
            logger.info(privateEcs.toString());
        } catch (PrivateECSException e) {
            logger.severe(Arrays.toString(e.getSuppressed()));
        }
        
        try{
            PublicECS publicEcs = findPublicECS(clientLocation);
            logger.info(publicEcs.toString());
        } catch (PublicECSException e) {
            logger.severe(Arrays.toString(e.getSuppressed()));
        }                
    }

    public static PrivateECS findPrivateECS(String currentLocation) throws InterruptedException {

        try (var scope = StructuredTaskScope.open(new PrivateChargeFinderScope())) {

            scope.fork(() -> PrivateECSLocator.greenEnergyLocator(currentLocation));
            scope.fork(() -> PrivateECSLocator.carElectricLocator(currentLocation));
            scope.fork(() -> PrivateECSLocator.electricChargeLocator(currentLocation));

            return scope.join();
        } catch (FailedException e) {            
            throw (PrivateECSException) e.getCause();
        }
    }

    public static PublicECS findPublicECS(String currentLocation) throws InterruptedException {

        try (var scope = StructuredTaskScope.open(new PublicChargeFinderScope())) {
            
            scope.fork(() -> PublicECSLocator.ecsMotelsLocator(currentLocation));
            scope.fork(() -> PublicECSLocator.ecsHighwayLocator(currentLocation));
            scope.fork(() -> PublicECSLocator.ecsMallParkingLocator(currentLocation));            
            scope.fork(() -> PublicECSLocator.ecsDriveInLocator(currentLocation));
            
            return scope.join();                                          
        } catch (FailedException e) {            
            throw (PublicECSException) e.getCause();
        }
    }
}
