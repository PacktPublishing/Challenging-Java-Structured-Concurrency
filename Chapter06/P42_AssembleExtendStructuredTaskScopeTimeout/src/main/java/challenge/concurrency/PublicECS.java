package challenge.concurrency;

public record PublicECS(String location, int distanceKm, 
        int chargingTimeMin, int availableInMin) implements Charger {}
