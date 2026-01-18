package challenge.concurrency;

public class HighwayContract {
    
    private final String roadContract;
    private final String tunnelContract;
    private final String bridgeContract;

    public HighwayContract(String roadContract, String tunnelContract, String bridgeContract) {
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
