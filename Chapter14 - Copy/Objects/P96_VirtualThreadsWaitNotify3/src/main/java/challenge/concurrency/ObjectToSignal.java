package challenge.concurrency;

import java.util.logging.Logger;

public class ObjectToSignal {

    private static final Logger logger = Logger.getLogger(ObjectToSignal.class.getName());

    private int signalCount;

    public void invokeWait() throws InterruptedException {
        
        synchronized (this) {

            signalCount = signalCount - 1;

            if (signalCount >= 0) {
                logger.warning(() -> Thread.currentThread().getName()
                        + " | A missed signal was detected!");
                return;
            }

            logger.info("Before invoking the wait() method ...");
            logger.info(() -> Thread.currentThread().getName() 
                    + " | " + Thread.currentThread().getState());
            wait();
            logger.info("After invoking the notify() method ...");
            logger.info(() -> Thread.currentThread().getName() 
                    + " | " + Thread.currentThread().getState());
        }
    }

    public void invokeNotify() {
        
        synchronized (this) {
            
            signalCount = signalCount + 1;
            
            logger.info(() -> "Signal counter value: " + signalCount);

            notify();
        }
    }
}