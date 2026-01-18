package challenge.concurrency;

import java.util.Random;

public final class PrivateECSLocator {
    
    public static PrivateECS greenEnergyLocator(String clientLocation) throws InterruptedException {
        
        Random random = new Random();
        boolean availableECS = random.nextBoolean();
        
        if(availableECS) {
            
            Thread.sleep(random.nextInt(500)); // simulate network frictions
            
            int distanceKm = random.nextInt(1, 5);
            int chargingTime = random.nextInt(5, 10);
            double pricePerMin = chargingTime * 0.50d;
            int availableInMin = random.nextInt(10, 25);
            
            return new PrivateECS("GreenEnergy", pricePerMin, distanceKm, chargingTime, availableInMin);
        }
        
        throw new PrivateECSException("GreenEnergy: No charging stations are available near the location, " + clientLocation);
    }
    
    public static PrivateECS carElectricLocator(String clientLocation) throws InterruptedException {
        
        Random random = new Random();
        boolean availableECS = random.nextBoolean();
        
        if(availableECS) {
            
            Thread.sleep(random.nextInt(500)); // simulate network frictions
            
            int distanceKm = random.nextInt(1, 5);
            int chargingTime = random.nextInt(7, 12);
            double pricePerMin = chargingTime * 0.40d;
            int availableInMin = random.nextInt(7, 20);
            
            return new PrivateECS("CarElectric", pricePerMin, distanceKm, chargingTime, availableInMin);
        }
        
        throw new PrivateECSException("CarElectric: No charging stations are available near the location, " + clientLocation);
    }
    
    public static PrivateECS electricChargeLocator(String clientLocation) throws InterruptedException {
        
        Random random = new Random();
        boolean availableECS = random.nextBoolean();
        
        if(availableECS) {
            
            Thread.sleep(random.nextInt(500)); // simulate network frictions
            
            int distanceKm = random.nextInt(1, 7);
            int chargingTime = random.nextInt(4, 8);
            double pricePerMin = chargingTime * 0.52d;
            int availableInMin = random.nextInt(10, 15);
            
            return new PrivateECS("ElectricCharge", pricePerMin, distanceKm, chargingTime, availableInMin);
        }
        
        throw new PrivateECSException("ElectricCharge: No charging stations are available near the location, " + clientLocation);
    }
}
