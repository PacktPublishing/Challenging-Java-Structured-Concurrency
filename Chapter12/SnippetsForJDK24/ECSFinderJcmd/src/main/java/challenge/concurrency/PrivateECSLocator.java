package challenge.concurrency;

import static challenge.concurrency.Main.CLIENT_LOCATION;
import static challenge.concurrency.Main.CE_PRIVATE_ECS_DISCOUNT;
import static challenge.concurrency.Main.CLIENT_PRIVATE_ECS_TICKET_ID;
import java.util.Random;
import java.util.logging.Logger;

public final class PrivateECSLocator {
    
    private static final Logger logger = Logger.getLogger(PrivateECSLocator.class.getName());
    
    public static PrivateECS greenEnergyLocator() throws InterruptedException {
        
        String clientLocationParam = CLIENT_LOCATION.get();
        int clientPrivateEcsTicketIdParam = CLIENT_PRIVATE_ECS_TICKET_ID.get();                  
           
        // The following code runs remotely as a web service - we trigger from here a request to it
        Random random = new Random();
        boolean availableECS = random.nextBoolean();
        
        Thread.sleep(15000 + random.nextInt(5000)); // added for thread dump
        
        if(availableECS) {
            
            Thread.sleep(500); // simulate network frictions
            
            int distanceKm = random.nextInt(1, 5);
            int chargingTime = random.nextInt(5, 10);
            double pricePerMin = chargingTime * 0.50d;
            int availableInMin = random.nextInt(10, 25);
            
            return new PrivateECS("GreenEnergy#" + clientPrivateEcsTicketIdParam,
                    pricePerMin, distanceKm, chargingTime, availableInMin);
        }
        
        throw new PrivateECSException("GreenEnergy#" + clientPrivateEcsTicketIdParam 
                + " : No charging stations are available near the location, " + clientLocationParam);
    }
    
    public static PrivateECS carElectricLocator() throws InterruptedException {
        
        String clientLocationParam = CLIENT_LOCATION.get();
        int clientPrivateEcsTicketIdParam = CLIENT_PRIVATE_ECS_TICKET_ID.get();
        double cePrivateEcsDiscountParam = CE_PRIVATE_ECS_DISCOUNT.get();
        
        // The following code runs remotely as a web service - we trigger from here a request to it
        Random random = new Random();
        boolean availableECS = random.nextBoolean();
        
        Thread.sleep(15000 + random.nextInt(5000)); // added for thread dump
       
        if(availableECS) {
            
            Thread.sleep(500); // simulate network frictions
            
            int distanceKm = random.nextInt(1, 5);
            int chargingTime = random.nextInt(7, 12);
            double pricePerMin = chargingTime * 0.40d;
            int availableInMin = random.nextInt(7, 20);
            
            if(cePrivateEcsDiscountParam > 0) {
                logger.info(() -> "CarElectric: Client #" + clientPrivateEcsTicketIdParam
                        + "! has a discount of " + cePrivateEcsDiscountParam);
                
                pricePerMin = pricePerMin - cePrivateEcsDiscountParam;
            }
            
            return new PrivateECS("CarElectric#" + clientPrivateEcsTicketIdParam
                    , pricePerMin, distanceKm, chargingTime, availableInMin);
        }
        
        throw new PrivateECSException("CarElectric#" + clientPrivateEcsTicketIdParam 
                + ": No charging stations are available near the location, " + clientLocationParam);
    }
    
    public static PrivateECS electricChargeLocator() throws InterruptedException {
        
        String clientLocationParam = CLIENT_LOCATION.get();
        int clientPrivateEcsTicketIdParam = CLIENT_PRIVATE_ECS_TICKET_ID.get();
      
        // The following code runs remotely as a web service - we trigger from here a request to it
        Random random = new Random();
        boolean availableECS = random.nextBoolean();
        
        Thread.sleep(15000 + random.nextInt(5000)); // added for thread dump
        
        if(availableECS) {
            
            Thread.sleep(500); // simulate network frictions
            
            int distanceKm = random.nextInt(1, 7);
            int chargingTime = random.nextInt(4, 8);
            double pricePerMin = chargingTime * 0.52d;
            int availableInMin = random.nextInt(10, 15);
            
            return new PrivateECS("ElectricCharge#" + clientPrivateEcsTicketIdParam
                    , pricePerMin, distanceKm, chargingTime, availableInMin);
        }
        
        throw new PrivateECSException("ElectricCharge# " + clientPrivateEcsTicketIdParam 
                + ": No charging stations are available near the location, " + clientLocationParam);
    }
}
