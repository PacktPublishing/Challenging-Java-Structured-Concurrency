package challenge.concurrency;

import java.util.AbstractMap.SimpleEntry;
import java.util.logging.Logger;
import java.util.stream.Gatherers;
import static java.util.stream.Gatherers.mapConcurrent;
import java.util.stream.Stream;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        ServiceStack ss = Stream.of(
                new SimpleEntry<>("MsgService", ServiceType.AMQP),
                new SimpleEntry<>("CrossService", ServiceType.XMPP),
                new SimpleEntry<>("IotService", ServiceType.MQTT))
                .gather(mapConcurrent(3, e -> {
                    logger.info(() -> Thread.currentThread().toString());
                    return new Service(e.getKey()).start(e.getValue());
                }))
                .gather(Gatherers.windowFixed(3))
                .map(services -> new ServiceStack(
                (Amqp) services.get(0), (Xmpp) services.get(1), (Mqtt) services.get(2)))
                .findFirst()
                .orElseThrow();

        logger.info(ss.toString());
    }
}
