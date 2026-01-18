package challenge.concurrency;

public sealed interface Charger permits ECS, PrivateECS, PublicECS {}
