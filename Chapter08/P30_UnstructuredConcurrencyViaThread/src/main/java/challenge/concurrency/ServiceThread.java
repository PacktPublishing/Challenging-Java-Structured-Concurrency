package challenge.concurrency;

public class ServiceThread extends Thread {

    private final Service service;
    private final ServiceType type;
    private Protocol protocol;

    public ServiceThread(
            Service service, ServiceType type) {
        this.service = service;
        this.type = type;
    }

    @Override
    public void run() {
        protocol = service.start(type);
    }

    public Protocol getProtocol() {
        return protocol;
    }
}
