package challenge.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        CompletableFuture<Mqtt> iotServiceCf = CompletableFuture.supplyAsync(() -> {
            return (Mqtt) new Service("IoTService").start(ServiceType.MQTT);
        });

        CompletableFuture<Amqp> msgServiceCf = CompletableFuture.supplyAsync(() -> {
            return (Amqp) new Service("MsgService").start(ServiceType.AMQP);
        });

        CompletableFuture<Xmpp> crossServiceCf = CompletableFuture.supplyAsync(() -> {
            return (Xmpp) new Service("CrossService").start(ServiceType.XMPP);
        });

        CompletableFuture.allOf(iotServiceCf, msgServiceCf, crossServiceCf).get(3, TimeUnit.SECONDS);

        ServiceStack ss = new ServiceStack(
                msgServiceCf.resultNow(), crossServiceCf.resultNow(), iotServiceCf.resultNow());

        logger.info(ss.toString());
    }
}
