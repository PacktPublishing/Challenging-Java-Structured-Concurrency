package challenge.concurrency;

public sealed interface Contract 
        permits RoadContract, TunnelContract, BridgeContract {}
