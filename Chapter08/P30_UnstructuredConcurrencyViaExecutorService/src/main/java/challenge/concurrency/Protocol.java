package challenge.concurrency;

public sealed interface Protocol permits Amqp, Mqtt, Xmpp {}