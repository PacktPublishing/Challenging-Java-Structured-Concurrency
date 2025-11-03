package challenge.concurrency;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        ServiceThread amqpThread = new ServiceThread(
                new Service("MsgService"), ServiceType.AMQP);
        ServiceThread xmppThread = new ServiceThread(
                new Service("CrossService"), ServiceType.XMPP);
        ServiceThread mqttThread = new ServiceThread(
                new Service("IoTService"), ServiceType.MQTT);                
                
        amqpThread.start();
        xmppThread.start();
        mqttThread.start();
                
        amqpThread.join();
        xmppThread.join();
        mqttThread.join();
        
        ServiceStack ss = new ServiceStack(
                (Amqp) amqpThread.getProtocol(),
                (Xmpp) xmppThread.getProtocol(),
                (Mqtt) mqttThread.getProtocol());
        
        logger.info(ss.toString());
    }       
}