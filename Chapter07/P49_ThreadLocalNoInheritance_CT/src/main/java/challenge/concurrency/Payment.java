package challenge.concurrency;

import java.util.logging.Logger;

public class Payment extends Thread {

    private static final Logger logger = Logger.getLogger(Payment.class.getName());
    
    @Override
    public void run() {
        
        logger.info(() -> "Payment operator " + Thread.currentThread().toString() 
                + " process " + User.voucher.get());
    }    
}