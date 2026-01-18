package challenge.concurrency;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    private static final ThreadLocal<String> TL = new ThreadLocal<>();
    private static final ScopedValue<String> SV = ScopedValue.newInstance();

    public static void welcomeTL() {
        logger.info(Thread.currentThread().toString());
        logger.info(() -> "Welcome, " + TL.get());
    }
    
    public static void byebyeTL() {
        logger.info(Thread.currentThread().toString());
        logger.info(() -> "Bye, bye, " + TL.get());
    }
    
    public static void welcomeSV() {
        logger.info(Thread.currentThread().toString());
        logger.info(() -> "Welcome, " + SV.orElse("you"));
    }
    
    public static void byebyeSV() {
        logger.info(Thread.currentThread().toString());
        logger.info(() -> "Bye, bye, " + SV.orElse("you"));
    }
    
    public static void main(String[] args) throws InterruptedException, Exception {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");        
        
        TL.set("Janel");
        welcomeTL();
        byebyeTL();
        
        ScopedValue.where(SV, "Janel").run(() -> welcomeSV());
        byebyeSV();                             
    }  
}