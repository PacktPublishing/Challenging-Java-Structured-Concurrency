package challenge.concurrency;

import static challenge.concurrency.Main.CLIENT_LOCATION;
import static challenge.concurrency.Main.PUBLIC_DRIVEIN_ECS_COUPON;
import static challenge.concurrency.Main.PUBLIC_HIGHWAY_ECS_COUPON;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public final class PublicECSLocator {

    private static final Logger logger = Logger.getLogger(PublicECSLocator.class.getName());

    public static List<PublicECS> ecsHighwayLocator() throws InterruptedException {

        String clientLocationParam = CLIENT_LOCATION.get();
        boolean publicHighwayEcsCouponParam = PUBLIC_HIGHWAY_ECS_COUPON.get();

        List<PublicECS> listOfEcs = new ArrayList<>();

        // The following code runs remotely as a web service - we trigger from here a request to it
        Random random = new Random();
        boolean availableECS = random.nextBoolean();

        if (availableECS) {
            if (publicHighwayEcsCouponParam) {

                Thread.sleep(random.nextInt(500)); // simulate network frictions

                for (int i = 0; i < random.nextInt(1, 10); i++) {
                    int distanceKm = random.nextInt(1, 5);
                    int chargingTime = random.nextInt(5, 10);
                    int availableInMin = random.nextInt(35, 50);

                    listOfEcs.add(new PublicECS("Highway", distanceKm, chargingTime, availableInMin));
                }

                return listOfEcs;
            } else {
                logger.warning("Sorry, the Highway ECS can be accessed only if you have a valid coupon");
            }
        }

        throw new PublicECSException("No charging stations are available on the highway near the location, " + clientLocationParam);
    }

    public static List<PublicECS> ecsMallParkingLocator() throws InterruptedException {

        String clientLocationParam = CLIENT_LOCATION.get();
        List<PublicECS> listOfEcs = new ArrayList<>();

        // The following code runs remotely as a web service - we trigger from here a request to it
        Random random = new Random();
        boolean availableECS = random.nextBoolean();

        if (availableECS) {

            Thread.sleep(random.nextInt(500)); // simulate network frictions

            for (int i = 0; i < random.nextInt(1, 10); i++) {
                int distanceKm = random.nextInt(3, 10);
                int chargingTime = random.nextInt(12, 17);
                int availableInMin = random.nextInt(60, 120);

                listOfEcs.add(new PublicECS("Parking Mall", distanceKm, chargingTime, availableInMin));
            }

            return listOfEcs;
        }

        throw new PublicECSException("No charging stations are available in the parking malls near the location, " + clientLocationParam);
    }

    public static List<PublicECS> ecsMotelsLocator() throws InterruptedException {

        String clientLocationParam = CLIENT_LOCATION.get();
        List<PublicECS> listOfEcs = new ArrayList<>();

        // The following code runs remotely as a web service - we trigger from here a request to it
        Random random = new Random();
        boolean availableECS = random.nextBoolean();

        if (availableECS) {

            Thread.sleep(random.nextInt(500)); // simulate network frictions

            for (int i = 0; i < random.nextInt(1, 10); i++) {
                int distanceKm = random.nextInt(3, 10);
                int chargingTime = random.nextInt(15, 25);
                int availableInMin = random.nextInt(15, 35);

                listOfEcs.add(new PublicECS("Motels", distanceKm, chargingTime, availableInMin));
            }

            return listOfEcs;
        }

        throw new PublicECSException("No charging stations are available in the parking motels near the location, " + clientLocationParam);
    }

    public static List<PublicECS> ecsDriveInLocator() throws InterruptedException {

        String clientLocationParam = CLIENT_LOCATION.get();
        boolean publicDriveinEcsCouponParam = PUBLIC_DRIVEIN_ECS_COUPON.get();

        List<PublicECS> listOfEcs = new ArrayList<>();

        // The following code runs remotely as a web service - we trigger from here a request to it
        Random random = new Random();
        boolean availableECS = random.nextBoolean();

        if (availableECS) {

            if (publicDriveinEcsCouponParam) {

                Thread.sleep(random.nextInt(500)); // simulate network frictions

                for (int i = 0; i < random.nextInt(1, 10); i++) {
                    int distanceKm = random.nextInt(1, 10);
                    int chargingTime = random.nextInt(15, 25);
                    int availableInMin = random.nextInt(120, 180);

                    listOfEcs.add(new PublicECS("Drive-in", distanceKm, chargingTime, availableInMin));
                }

                return listOfEcs;
            } else {
                logger.warning("Sorry, the Drive-in ECS can be accessed only if you have a valid coupon");
            }
        }

        throw new PublicECSException("No charging stations are available in drive-ins near the location, " + clientLocationParam);
    }
}
