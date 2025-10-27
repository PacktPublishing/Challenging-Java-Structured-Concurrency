package challenge.concurrency;

public enum HighwaySignPartType {
   ROAD(new RoadContract()), TUNNEL(new TunnelContract()), BRIDGE(new BridgeContract());
   
   private final Contract contract;
   
   HighwaySignPartType(Contract contract) {
       this.contract = contract;
   }
   
   public Contract getContract() {
       return this.contract;
   }
}
