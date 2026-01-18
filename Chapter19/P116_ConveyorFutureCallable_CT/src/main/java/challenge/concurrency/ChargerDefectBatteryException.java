package challenge.concurrency;

import java.util.concurrent.ExecutionException;

public class ChargerDefectBatteryException extends ExecutionException {

    private static final long serialVersionUID = 1L;

    public ChargerDefectBatteryException() {
        super();
    }

    public ChargerDefectBatteryException(String message) {
        super(message);
    }

    public ChargerDefectBatteryException(Throwable cause) {
        super(cause);
    }

    public ChargerDefectBatteryException(String message, Throwable cause) {
        super(message, cause);
    }
}
