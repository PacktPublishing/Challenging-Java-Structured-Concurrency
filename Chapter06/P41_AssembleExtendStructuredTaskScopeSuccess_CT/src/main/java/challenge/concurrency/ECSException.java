package challenge.concurrency;

public class ECSException extends RuntimeException {

    public ECSException(String msg) {
        super(msg);
    }        
    
    public ECSException(Throwable t) {
        super(t);
    }
}
