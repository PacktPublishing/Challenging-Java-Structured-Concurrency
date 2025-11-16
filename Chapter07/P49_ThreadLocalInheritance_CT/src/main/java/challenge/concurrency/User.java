package challenge.concurrency;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class User extends Thread {

    private static final Logger logger = Logger.getLogger(User.class.getName());    
    
    private static final AtomicInteger voucherId = new AtomicInteger(0);
    
    public static final InheritableThreadLocal<String> voucher = new InheritableThreadLocal<>() {
        @Override
        public String childValue(String parentValue) {
            return parentValue + "(Valid)";  
        }
    };            
    
    @Override
    public void run() {
        
        voucher.set("Voucher#" + voucherId.incrementAndGet());
        
        logger.info(() -> "User " + Thread.currentThread().toString() 
                + " will pay with " + voucher.get());
        
        Payment payment = new Payment();
        payment.start();
        
        try { payment.join(); } catch (InterruptedException ex) {}
        
        voucher.remove();
    }                
}