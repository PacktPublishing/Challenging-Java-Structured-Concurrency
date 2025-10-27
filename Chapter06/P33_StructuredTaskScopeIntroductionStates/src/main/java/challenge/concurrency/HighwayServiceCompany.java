package challenge.concurrency;

import java.util.logging.Logger;

public class HighwayServiceCompany {
    
    private static final Logger logger = Logger.getLogger(HighwayServiceCompany.class.getName());
    
    private final String name;    

    public HighwayServiceCompany(String name) {
        this.name = name;
    }
    
    public Contract signPartType(HighwaySignPartType bpt) {   
        
        if (Math.random() < 0.5d) { throw new RuntimeException(bpt.name()); }
        
        logger.info(() -> "Preparing " + bpt 
                + " contract by " + name
                + " (" + Thread.currentThread().toString() + ")");
        
        return bpt.getContract();
    }

    public String getName() {
        return name;
    }        
}
