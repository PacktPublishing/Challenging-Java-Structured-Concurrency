package challenge.concurrency;

import java.util.logging.Logger;

public class User {
    
    private static final Logger logger = Logger.getLogger(User.class.getName());
    
    public static void user() {
        ScopedValue.where(Main.SV, "user"); // this returns another Carrier
        logger.info(Main.SV.get());        
    }
}
