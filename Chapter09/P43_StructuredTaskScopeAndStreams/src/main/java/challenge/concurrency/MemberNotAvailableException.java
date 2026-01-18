package challenge.concurrency;

public class MemberNotAvailableException extends RuntimeException {

    public MemberNotAvailableException(String message) {
        super(message);
    }
}
