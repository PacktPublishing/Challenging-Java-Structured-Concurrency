package challenge.concurrency;

public record ECS(PrivateECS privateECS, PublicECS publicECS) implements Charger {}
