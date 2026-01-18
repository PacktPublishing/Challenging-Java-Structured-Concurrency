package challenge.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
            
        CompletableFuture<ServiceStack> servicesCf 
                = serviceCf("IoTService", ServiceType.MQTT)
                .thenCombine(serviceCf("MsgService", ServiceType.AMQP), 
                        (iot, msg) -> {
                            Protocol cross = new Service("CrossService").start(ServiceType.XMPP);
                            
                            return  new ServiceStack((Amqp) msg, (Xmpp) cross, (Mqtt) iot);
                        });
                
        ServiceStack ss = servicesCf.get();
        logger.info(ss.toString());        
    }     
    
    private static CompletableFuture<Protocol> serviceCf(String sn, ServiceType st) {
        return CompletableFuture.supplyAsync(() -> {
            return new Service(sn).start(st);
        });
    }
}