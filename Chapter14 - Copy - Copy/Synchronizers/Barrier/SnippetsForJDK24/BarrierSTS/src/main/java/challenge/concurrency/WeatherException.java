package challenge.concurrency;

public class WeatherException extends Exception {

    private static final long serialVersionUID = -1L;

    public WeatherException() {
    }

    public WeatherException(String message) {
        super(message);
    }
}
