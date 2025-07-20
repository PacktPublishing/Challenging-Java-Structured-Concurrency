package challenge.concurrency;

import java.util.logging.Logger;

public class HighwayServiceCompany {
    
    private static final Logger logger = Logger.getLogger(HighwayServiceCompany.class.getName());
    
    private final String name;    

    public HighwayServiceCompany(String name) {
        this.name = name;
    }
    
    public String signPartType(HighwaySignPartType bpt) {
        logger.info(() -> "Preparing " + bpt 
                + " contract by " + name
                + " (" + Thread.currentThread().toString() + ")");
        return name + " - " + bpt;
    }

    public String getName() {
        return name;
    }        
}
