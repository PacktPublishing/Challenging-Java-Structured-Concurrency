package challenge.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class PublicECSLocator {
    
    public static List<PublicECS> ecsHighwayLocator(String clientLocation) throws InterruptedException {
        
        List<PublicECS> listOfEcs = new ArrayList<>();
        
        Random random = new Random();
        boolean availableECS = random.nextBoolean();                
      
        if(availableECS) {
            
            Thread.sleep(random.nextInt(1000)); // random timeout
            
            Thread.sleep(random.nextInt(500)); // simulate network frictions
            
            for (int i = 0; i < random.nextInt(1, 10); i++) {
                int distanceKm = random.nextInt(1, 5);
                int chargingTime = random.nextInt(5, 10);            
                int availableInMin = random.nextInt(35, 50);
                
                listOfEcs.add(new PublicECS("Highway", distanceKm, chargingTime, availableInMin));
            }
            
            return listOfEcs;
        }
        
        throw new PublicECSException("No charging stations are available on the highway near the location, " + clientLocation);
    }
    
    public static List<PublicECS> ecsMallParkingLocator(String clientLocation) throws InterruptedException {
        
        List<PublicECS> listOfEcs = new ArrayList<>();
        
        Random random = new Random();
        boolean availableECS = random.nextBoolean();
        
        if(availableECS) {
            
            Thread.sleep(random.nextInt(1000)); // random timeout
            
            Thread.sleep(random.nextInt(500)); // simulate network frictions
            
            for (int i = 0; i < random.nextInt(1, 10); i++) {
                int distanceKm = random.nextInt(3, 10);
                int chargingTime = random.nextInt(12, 17);            
                int availableInMin = random.nextInt(60, 120);
                
                listOfEcs.add(new PublicECS("Parking Mall", distanceKm, chargingTime, availableInMin));
            }
            
            return listOfEcs;
        }
        
        throw new PublicECSException("No charging stations are available in the parking malls near the location, " + clientLocation);
    }
    
    public static List<PublicECS> ecsMotelsLocator(String clientLocation) throws InterruptedException {
        
        List<PublicECS> listOfEcs = new ArrayList<>();
        
        Random random = new Random();
        boolean availableECS = random.nextBoolean();
        
        if(availableECS) {
            
            Thread.sleep(random.nextInt(1000)); // random timeout
            
            Thread.sleep(random.nextInt(500)); // simulate network frictions
            
            for (int i = 0; i < random.nextInt(1, 10); i++) {
                int distanceKm = random.nextInt(3, 10);
                int chargingTime = random.nextInt(15, 25);            
                int availableInMin = random.nextInt(115, 135);
                
                listOfEcs.add(new PublicECS("Motels", distanceKm, chargingTime, availableInMin));
            }
            
            return listOfEcs;
        }
        
        throw new PublicECSException("No charging stations are available in the parking motels near the location, " + clientLocation);
    }
    
    public static List<PublicECS> ecsDriveInLocator(String clientLocation) throws InterruptedException {
        
        List<PublicECS> listOfEcs = new ArrayList<>();
        
        Random random = new Random();
        boolean availableECS = random.nextBoolean();
        
        if(availableECS) {
            
            Thread.sleep(random.nextInt(1000)); // random timeout
            
            Thread.sleep(random.nextInt(500)); // simulate network frictions
            
            for (int i = 0; i < random.nextInt(1, 10); i++) {
                int distanceKm = random.nextInt(1, 10);
                int chargingTime = random.nextInt(15, 25);            
                int availableInMin = random.nextInt(120, 180);
                
                listOfEcs.add(new PublicECS("Drive-in", distanceKm, chargingTime, availableInMin));
            }
            
            return listOfEcs;
        }
        
        throw new PublicECSException("No charging stations are available in drive-ins near the location, " + clientLocation);
    }
}