package challenge.concurrency;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class BadInterval {
    
    private static final Logger logger = Logger.getLogger(BadInterval.class.getName());
    
    private final AtomicInteger left = new AtomicInteger(0);
    private final AtomicInteger right = new AtomicInteger(0);

    public void setLeft(int newLeft) {
        
        if (newLeft > right.get()) {
            // logger.warning("Left cannot be bigger than right");
            return;
        }
        
        left.set(newLeft);
    }

    public void setRight(int newRight) {
        
        if (newRight < left.get()) {
            // logger.warning("Right cannot be smaller than left");
            return;
        }
        
        right.set(newRight);
    }   

    public AtomicInteger getLeft() {
        return left;
    }

    public AtomicInteger getRight() {
        return right;
    }        
}