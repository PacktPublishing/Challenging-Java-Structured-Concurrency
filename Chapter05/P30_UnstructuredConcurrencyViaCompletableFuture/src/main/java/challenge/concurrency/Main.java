package challenge.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
            
        CompletableFuture<HighwayContract> contractCf 
                = roadConnectionsCf("TunnelsCo", HighwaySignPartType.TUNNEL)
                .thenCombine(roadConnectionsCf("TheBridges", HighwaySignPartType.BRIDGE), 
                        (t, b) -> {
                            String r = new HighwayServiceCompany("BestRoads")
                                    .signPartType(HighwaySignPartType.ROAD);
                            
                            return  new HighwayContract(r, t, b);
                        });
                
        HighwayContract contract = contractCf.get();
        logger.info(contract.toString());        
    }     
    
    private static CompletableFuture<String> roadConnectionsCf(
            String cn, HighwaySignPartType hspt) {
        return CompletableFuture.supplyAsync(() -> {
            return new HighwayServiceCompany(cn).signPartType(hspt);
        });
    }
}