package challenge.concurrency;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
                
        // StructuredTaskScope<Protocol, Void>
        try (var scope = StructuredTaskScope.open()) {                    

           Subtask<Mqtt> mqttSubtask = scope.fork(() -> 
                (Mqtt) new Service("IoTService").start(ServiceType.MQTT));                              
           Subtask<Amqp> amqpSubtask = scope.fork(() ->
                (Amqp) new Service("MsgService").start(ServiceType.AMQP));                        
           Subtask<Xmpp> xmppSubtask = scope.fork(() ->
                (Xmpp) new Service("CrossService").start(ServiceType.XMPP));                        
                 
           scope.join(); // Join subtasks, propagating exceptions
           
           // All subtasks have succeeded, so compose their results
           ServiceStack ss = new ServiceStack(
                   amqpSubtask.get(), xmppSubtask.get(), mqttSubtask.get());
        
           logger.info(ss.toString());
        }
    }       
}