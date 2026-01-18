package challenge.concurrency;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        ObjectToSignal o = new ObjectToSignal();

        Thread vthread1 = Thread.ofVirtual().name("vt-1").unstarted(() -> {
            try {            
                o.invokeWait();
            } catch (InterruptedException ex) {}
        });
        
        Thread vthread2 = Thread.ofVirtual().name("vt-2").unstarted(() -> {           
                o.invokeNotify();
        });                
        
        // good signal
        /*
        vthread1.start();
        Thread.sleep(500); // starting 'vt-1'              
        logger.info(() -> vthread1.getName() + " | " + vthread1.getState());
        
        vthread2.start();        
        Thread.sleep(500); // starting 'vt-2'               
        logger.info(() -> vthread1.getName() + " | " + vthread1.getState());
        */
        
        // missed signal        
        vthread2.start();        
        Thread.sleep(500); // starting 'vt-2'
        
        vthread1.start();
        Thread.sleep(500); // starting 'vt-1'        
        logger.info(() -> vthread1.getName() + " | " + vthread1.getState());               
        
        vthread1.join(); // notify() call was missed, so 'vt-1' will wait forever
    }
}