package challenge.concurrency;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.FailedException;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.StructuredTaskScope.Subtask.State;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws Throwable {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
                
        Subtask<Mqtt> mqttSubtask = null;
        Subtask<Amqp> amqpSubtask = null;
        Subtask<Xmpp> xmppSubtask = null;
        
        // StructuredTaskScope<Protocol, Void>
        try (var scope = StructuredTaskScope.open()) {                    

           mqttSubtask = scope.fork(() -> 
                (Mqtt) new Service("IoTService").start(ServiceType.MQTT));                              
           amqpSubtask = scope.fork(() ->
                (Amqp) new Service("MsgService").start(ServiceType.AMQP));                        
           xmppSubtask = scope.fork(() ->
                (Xmpp) new Service("CrossService").start(ServiceType.XMPP));                        
                 
           scope.join(); // Join subtasks, propagating exceptions
           
           // All subtasks have succeeded, so compose their results
           ServiceStack ss = new ServiceStack(
                   amqpSubtask.get(), xmppSubtask.get(), mqttSubtask.get());
        
           logger.info(ss.toString());
        } catch (FailedException e) {
            
            State mqttSubtaskState = mqttSubtask.state();
            State amqpSubtaskState = amqpSubtask.state();
            State xmppSubtaskState = xmppSubtask.state();
                       
            logger.info(() -> "MQTT-based service state: " + mqttSubtaskState);
            logger.info(() -> "AMWP-based service state: " + amqpSubtaskState);
            logger.info(() -> "XMPP-based service state: " + xmppSubtaskState);
            
            throw e.getCause();
        }
    }       
}