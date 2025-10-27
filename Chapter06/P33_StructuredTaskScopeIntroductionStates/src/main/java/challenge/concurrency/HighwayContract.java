package challenge.concurrency;

public class HighwayContract {
    
    private final RoadContract roadContract;
    private final TunnelContract tunnelContract;
    private final BridgeContract bridgeContract;

    public HighwayContract(RoadContract roadContract, 
            TunnelContract tunnelContract, BridgeContract bridgeContract) {
        this.roadContract = roadContract;
        this.tunnelContract = tunnelContract;
        this.bridgeContract = bridgeContract;
    }

    @Override
    public String toString() {
        return "HighwayContract{" + "roadContract=" + roadContract 
                + ", tunnelContract=" + tunnelContract + ", bridgeContract=" + bridgeContract + '}';
    }           
}
