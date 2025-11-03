package challenge.concurrency;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        try (ExecutorService executor = Executors.newFixedThreadPool(3)) {
            Future<Protocol> mqttFuture = executor.submit(()
                    -> new Service("IoTService").start(ServiceType.MQTT));
            Future<Protocol> amqpFuture = executor.submit(()
                    -> new Service("MsgService").start(ServiceType.AMQP));
            Future<Protocol> xmppFuture = executor.submit(()
                    -> new Service("CrossService").start(ServiceType.XMPP));

            ServiceStack ss = new ServiceStack(
                    (Amqp) amqpFuture.get(),
                    (Xmpp) xmppFuture.get(),
                    (Mqtt) mqttFuture.get());

            logger.info(ss.toString());
        }
    }
}
