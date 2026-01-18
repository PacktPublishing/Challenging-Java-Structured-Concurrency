package challenge.concurrency;

public class ServiceStack {
    
    private final Amqp amqp;
    private final Xmpp xmpp;
    private final Mqtt mqtt;

    public ServiceStack(Amqp amqp, Xmpp xmpp, Mqtt mqtt) {
        this.amqp = amqp;
        this.xmpp = xmpp;
        this.mqtt = mqtt;
    }

    @Override
    public String toString() {
        return "ServiceStack{" + "amqp=" + amqp + ", xmpp=" + xmpp + ", mqtt=" + mqtt + '}';
    }          
}
