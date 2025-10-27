package challenge.concurrency;

import java.util.Arrays;
import java.util.Comparator;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNullElse;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import static java.util.concurrent.StructuredTaskScope.Subtask.State.FAILED;
import static java.util.concurrent.StructuredTaskScope.Subtask.State.SUCCESS;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        String clientLocation = "123 East 55st Street"; // shared by the client

        try {
            Charger ecsCharger = findECS(clientLocation);
            logger.info(ecsCharger.toString());
        } catch (ECSException e) {
            logger.severe(Arrays.toString(e.getSuppressed()));
        }
    }

    public static Charger findECS(String currentLocation) throws InterruptedException {

        try (StructuredTaskScope scope = new StructuredTaskScope<Charger>()) {

            Subtask<PrivateECS> privateECSSubtask
                    = scope.fork(() -> findPrivateECS(currentLocation));
            Subtask<PublicECS> publicECSSubtask
                    = scope.fork(() -> findPublicECS(currentLocation));

            scope.join();

            PrivateECS privateECS = privateECSSubtask.state() == SUCCESS ? privateECSSubtask.get() : null;
            PublicECS publicECS = publicECSSubtask.state() == SUCCESS ? publicECSSubtask.get() : null;

            if (nonNull(privateECS) && nonNull(publicECS)) {
                return new ECS(privateECS, publicECS);
            }

            try {
                return requireNonNullElse(privateECS, publicECS);
            } catch (NullPointerException e) {
                ECSException ecsException = new ECSException("No ECS found");
                ecsException.addSuppressed(privateECSSubtask.exception());
                ecsException.addSuppressed(publicECSSubtask.exception());

                throw ecsException;
            }
        }
    }

    private static PrivateECS findPrivateECS(String currentLocation) throws InterruptedException {

        try (StructuredTaskScope scope = new StructuredTaskScope<PrivateECS>()) {

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

    private static PublicECS findPublicECS(String currentLocation) throws InterruptedException {

        try (PublicChargeFinderScope scope = new PublicChargeFinderScope()) {

            scope.fork(() -> PublicECSLocator.ecsMotelsLocator(currentLocation));
            scope.fork(() -> PublicECSLocator.ecsHighwayLocator(currentLocation));
            scope.fork(() -> PublicECSLocator.ecsMallParkingLocator(currentLocation));
            scope.fork(() -> PublicECSLocator.ecsDriveInLocator(currentLocation));

            scope.join();

            return scope.fastestPublicECS();
        }
    }
}