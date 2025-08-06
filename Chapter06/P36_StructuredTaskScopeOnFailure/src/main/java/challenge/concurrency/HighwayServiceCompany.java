package challenge.concurrency;

import java.util.logging.Logger;

public class HighwayServiceCompany {
    
    private static final Logger logger = Logger.getLogger(HighwayServiceCompany.class.getName());
    
    private final String name;    

    public HighwayServiceCompany(String name) {
        this.name = name;
    }
    
    public String signPartType(HighwaySignPartType bpt) {       
        
        if(Math.random() < 0.5d) { throw new RuntimeException(
              "Something went wrong when signing the contract " + bpt);}
        
        logger.info(() -> "Preparing " + bpt 
                + " contract by " + name
                + " (" + Thread.currentThread().toString() + ")");
        return name + " - " + bpt;
    }

    public String getName() {
        return name;
    }        
}
