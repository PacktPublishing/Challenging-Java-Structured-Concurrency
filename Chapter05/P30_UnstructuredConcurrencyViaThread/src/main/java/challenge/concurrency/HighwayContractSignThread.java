package challenge.concurrency;

import java.util.logging.Logger;

public class HighwayContractSignThread extends Thread {
    
    private static final Logger logger = Logger.getLogger(HighwayContractSignThread.class.getName());
    
    private final HighwayServiceCompany serviceCompany;
    private final HighwaySignPartType signPartType;
    private String highwayPart;

    public HighwayContractSignThread(
            HighwayServiceCompany serviceCompany, HighwaySignPartType signPartType) {
        this.serviceCompany = serviceCompany;
        this.signPartType = signPartType;
    }
    
    @Override
    public void run() {
        logger.info(() -> "Preparing " + signPartType 
                + " contract by " + serviceCompany.getName()
                + " (" + Thread.currentThread().toString() + ")");
        highwayPart = serviceCompany.signHighwayPart(signPartType);
    }

    public String getHighwayPart() {
        return highwayPart;
    }        
}
