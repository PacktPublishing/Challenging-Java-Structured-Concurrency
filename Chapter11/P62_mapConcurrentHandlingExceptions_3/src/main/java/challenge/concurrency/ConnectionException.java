package challenge.concurrency;

public class ConnectionException extends Exception {

    private final ConnectionStatus status;

    public ConnectionException(ConnectionStatus status) {
        this.status = status;
    }

    public ConnectionStatus getStatus() {
        return status;
    }
}
