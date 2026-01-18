package challenge.concurrency;

public enum ServiceType {
    AMQP(new Amqp()), MQTT(new Mqtt()), XMPP(new Xmpp());

    private final Protocol protocol;

    ServiceType(Protocol protocol) {
        this.protocol = protocol;
    }

    public Protocol getProtocol() {
        return this.protocol;
    }
}
