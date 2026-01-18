package challenge.concurrency;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import static java.util.concurrent.Future.State.SUCCESS;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

         try (ExecutorService executor = Executors.newFixedThreadPool(3)) {            
            
            List<Future<Protocol>> serviceFutures = executor.invokeAll(
                    List.of(() -> new Service("MsgService").start(ServiceType.AMQP),                            
                            () -> new Service("CrossService").start(ServiceType.XMPP),
                            () -> new Service("IoTService").start(ServiceType.MQTT)),
                    3, TimeUnit.SECONDS
            );
           
            ServiceStack ss = new ServiceStack(
                    serviceFutures.get(0).state() == SUCCESS ? (Amqp) serviceFutures.get(0).resultNow() : null,
                    serviceFutures.get(1).state() == SUCCESS ? (Xmpp) serviceFutures.get(1).resultNow() : null,
                    serviceFutures.get(2).state() == SUCCESS ? (Mqtt) serviceFutures.get(2).resultNow() : null
            );

            logger.info(ss.toString());
        }
    }
}
