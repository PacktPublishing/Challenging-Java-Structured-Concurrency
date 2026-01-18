package challenge.concurrency;

import com.sun.management.HotSpotDiagnosticMXBean;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import static java.util.concurrent.StructuredTaskScope.Subtask.State.FAILED;
import static java.util.concurrent.StructuredTaskScope.Subtask.State.SUCCESS;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static final ScopedValue<String> USERNAME = ScopedValue.newInstance();
    public static final ScopedValue<String> CLIENT_LOCATION = ScopedValue.newInstance();
    public static final ScopedValue<Boolean> PUBLIC_HIGHWAY_ECS_COUPON = ScopedValue.newInstance();
    public static final ScopedValue<Boolean> PUBLIC_DRIVEIN_ECS_COUPON = ScopedValue.newInstance();
    public static final ScopedValue<Double> CE_PRIVATE_ECS_DISCOUNT = ScopedValue.newInstance();
    public static final ScopedValue<Integer> CLIENT_PRIVATE_ECS_TICKET_ID = ScopedValue.newInstance();

    // Data collected from user interface (from user request)
    private static final String username = Math.random() < 0.5 ? "fabio88" : "anonymous";             // login user
    private static final String clientLocation = Math.random() < 0.5 ? "123 East 55st Street" : null; // location shared by the client
    private static final double cePrivateEcsDiscount = Math.random() * 2;                             // CarElectric private ECS discount
    private static final int clientPrivateEcsTicketId = (int) (Math.random() * 1000);                 // client private ECS ticket
    private static final boolean validPublicHighwayEcsCoupon = Math.random() < 0.5;                   // public highway ECS coupon
    private static final boolean validPublicDriveinEcsCoupon = Math.random() < 0.5;                   // public drive-in ECS coupon

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        logger.info("--------------------------------------");
        logger.info(() -> "Collected username: " + username);
        logger.info(() -> "Collected client location: " + clientLocation);
        logger.info(() -> "Collected CarElectric private ECS discount: " + cePrivateEcsDiscount);
        logger.info(() -> "Collected client private ECS ticket: #" + clientPrivateEcsTicketId);        
        logger.info(() -> "Collected valid public highway ECS coupon: " + validPublicHighwayEcsCoupon);
        logger.info(() -> "Collected valid public drive-in ECS coupon: " + validPublicDriveinEcsCoupon);
        logger.info("--------------------------------------");

        Charger ecsCharger = null;
        try {
            ecsCharger = ScopedValue.where(USERNAME, username)
                    .where(CLIENT_LOCATION, clientLocation)
                    .call(() -> findECS());
        } catch (ECSException e) {
            logger.severe(() -> e + "  " + Arrays.toString(e.getSuppressed()));
        }

        if (ecsCharger != null) {
            logger.info(ecsCharger.toString());
        }
    }

    public static Charger findECS() throws InterruptedException {

        if (CLIENT_LOCATION.get() != null) {

            try (ChargeFinderScope scope = new ChargeFinderScope(
                    "ECSScope", Thread.ofVirtual().name("ecs-", 0).factory())) {

                if (!USERNAME.get().equals("anonymous")) {
                    scope.fork(() -> ScopedValue.where(CLIENT_PRIVATE_ECS_TICKET_ID, clientPrivateEcsTicketId)
                            .call(() -> findPrivateECS()));
                } else {
                    logger.warning("ECSFinder: Private ECS service can be accessed only by members");
                }

                scope.fork(() -> findPublicECS());
                
                HotSpotDiagnosticMXBean mBean = ManagementFactory
                         .getPlatformMXBean(HotSpotDiagnosticMXBean.class);
                try {
                    mBean.dumpThreads(Path.of("ECSScopeTD.json")
                            .toAbsolutePath().toString(),
                            HotSpotDiagnosticMXBean.ThreadDumpFormat.JSON);
                } catch (IOException ex) {}

                scope.join();

                return scope.bestECS();
            }
        } else {
            throw new ECSException("ECSFinder: You must share your location");
        }
    }

    private static PrivateECS findPrivateECS() throws InterruptedException {

        logger.info(() -> "PrivateECS: Processing request of " + USERNAME.get());

        try (StructuredTaskScope scope = new StructuredTaskScope<PrivateECS>(
                "PrivateECSScope", Thread.ofVirtual().name("privateECS-", 0).factory())) {

            Subtask<PrivateECS> greenEnergySubtask = scope.fork(()
                    -> PrivateECSLocator.greenEnergyLocator());
            Subtask<PrivateECS> carElectricSubtask = scope.fork(()
                    -> ScopedValue.where(CE_PRIVATE_ECS_DISCOUNT, cePrivateEcsDiscount)
                            .call(() -> PrivateECSLocator.carElectricLocator()));
            Subtask<PrivateECS> electricChargeSubtask = scope.fork(()
                    -> PrivateECSLocator.electricChargeLocator());

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

    private static PublicECS findPublicECS() throws InterruptedException {

        logger.info(() -> "PublicECS: Processing request of " + USERNAME.get());

        try (PublicChargeFinderScope scope = new PublicChargeFinderScope(
                "PublicECSScope", Thread.ofVirtual().name("publicECS-", 0).factory())) {

            scope.fork(() -> PublicECSLocator.ecsMotelsLocator());
            scope.fork(() -> ScopedValue.where(PUBLIC_HIGHWAY_ECS_COUPON, validPublicHighwayEcsCoupon)
                    .call(() -> PublicECSLocator.ecsHighwayLocator()));
            scope.fork(() -> PublicECSLocator.ecsMallParkingLocator());
            scope.fork(() -> ScopedValue.where(PUBLIC_DRIVEIN_ECS_COUPON, validPublicDriveinEcsCoupon)
                    .call(() -> PublicECSLocator.ecsDriveInLocator()));

            scope.join();

            return scope.fastestPublicECS();
        }
    }
}
