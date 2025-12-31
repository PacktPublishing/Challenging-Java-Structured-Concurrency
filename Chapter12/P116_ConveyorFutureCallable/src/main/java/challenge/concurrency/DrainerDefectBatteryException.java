package challenge.concurrency;

import java.util.concurrent.ExecutionException;

public class DrainerDefectBatteryException extends ExecutionException {

    private static final long serialVersionUID = 1L;

    public DrainerDefectBatteryException() {
        super();
    }

    public DrainerDefectBatteryException(String message) {
        super(message);
    }

    public DrainerDefectBatteryException(Throwable cause) {
        super(cause);
    }

    public DrainerDefectBatteryException(String message, Throwable cause) {
        super(message, cause);
    }
}
