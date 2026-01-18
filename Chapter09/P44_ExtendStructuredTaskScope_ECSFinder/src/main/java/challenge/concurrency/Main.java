package challenge.concurrency;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.FailedException;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import static java.util.concurrent.StructuredTaskScope.Subtask.State.FAILED;
import static java.util.concurrent.StructuredTaskScope.Subtask.State.SUCCESS;
import java.util.logging.Logger;
import java.util.stream.Stream;

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

        try (var scope = StructuredTaskScope.open(Joiner.<PrivateECS>awaitAll()))  {

            Subtask<PrivateECS> greenEnergySubtask = scope.fork(()
                    -> PrivateECSLocator.greenEnergyLocator(currentLocation));
            Subtask<PrivateECS> carElectricSubtask = scope.fork(()
                    -> PrivateECSLocator.carElectricLocator(currentLocation));
            Subtask<PrivateECS> electricChargeSubtask = scope.fork(()
                    -> PrivateECSLocator.electricChargeLocator(currentLocation));

            scope.join();

            PrivateECS ecs = Stream.of(greenEnergySubtask, carElectricSubtask, electricChargeSubtask)
                    .filter(t -> t.state() == SUCCESS)
                    .<PrivateECS>mapMulti((t, c) -> {
                        c.accept((PrivateECS) t.get());
                    })
                    .min(Comparator.comparingDouble(PrivateECS::pricePerMin))
                    .orElseThrow(() -> {
                        PrivateECSException exceptionWrapper
                                = new PrivateECSException("Private ECS exception");
                        Stream.of(greenEnergySubtask, carElectricSubtask, electricChargeSubtask)
                                .filter(t -> t.state() == FAILED)
                                .<Throwable>mapMulti((t, c) -> {
                                    c.accept(t.exception());
                                }).forEach(exceptionWrapper::addSuppressed);
                        throw exceptionWrapper;
                    });

            return ecs;
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
