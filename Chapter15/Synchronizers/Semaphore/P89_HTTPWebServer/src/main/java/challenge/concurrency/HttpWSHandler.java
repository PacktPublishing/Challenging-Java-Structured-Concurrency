package challenge.concurrency;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

public class HttpWSHandler implements HttpHandler {

    private final static Logger logger = Logger.getLogger(HttpWSHandler.class.getName());

    private final static int PERMITS = 30;
    private final static AtomicLong rid = new AtomicLong();
    private final static Semaphore semaphore = new Semaphore(PERMITS);

    private static final Callable<String> task = () -> {

        String response = null;

        try {
            Thread.sleep(300);
            response = "Request id_" + rid.incrementAndGet();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        return response;
    };

    private final boolean useLocking;

    public HttpWSHandler(boolean useLocking) {
        this.useLocking = useLocking;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String response;

        if (useLocking) {
            response = requestWithLocking();
        } else {
            response = requestNoLocking();
        }

        logger.info(() -> response + " | " + Thread.currentThread().toString());

        exchange.sendResponseHeaders(
                200, response != null ? response.length() : 0);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response != null ? response.getBytes() : new byte[0]);
        }
    }

    private String requestWithLocking() {

        try {
            semaphore.acquire();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        try {
            return task.call();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            semaphore.release();
        }
    }

    private String requestNoLocking() {

        try {
            return task.call();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
