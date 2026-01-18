package challenge.concurrency;

import java.util.logging.Logger;

public class Service {

    private static final Logger logger = Logger.getLogger(Service.class.getName());

    private final String name;

    public Service(String name) {
        this.name = name;
    }

    public Protocol start(ServiceType st) {

        if (Math.random() < 0.5d) { throw new RuntimeException(name); }

        logger.info(() -> "Starting " + name + " by " + Thread.currentThread().toString());

        return st.getProtocol();
    }

    public String getName() {
        return name;
    }
}
