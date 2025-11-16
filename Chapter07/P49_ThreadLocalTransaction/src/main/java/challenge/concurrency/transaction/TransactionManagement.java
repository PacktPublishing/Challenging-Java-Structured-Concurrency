package challenge.concurrency.transaction;

import java.sql.SQLException;
import java.util.logging.Logger;

public final class TransactionManagement implements TransactionContext {
    
    private static final Logger logger = Logger.getLogger(TransactionManagement.class.getName());        
    
    private TransactionManagement() {
        throw new AssertionError("Cannot be instantiated");
    }
    
    public static void begin() throws SQLException {
        
        logger.info(() -> "Begin transaction " + TRANSACTION_ID.get() 
                + " | " + Thread.currentThread());
        
        if (Math.random() < 0.2d) { throw new SQLException("Begin transaction exception"); } 
    }
    
    public static void commit() throws SQLException {
        
        logger.info(() -> "Commit transaction " + TRANSACTION_ID.get() 
                + " | " + Thread.currentThread());
        
        if (Math.random() < 0.2d) { throw new SQLException("Commit transaction exception"); } 
        
        TRANSACTION_ID.remove();
    }
    
    public static void rollback() throws SQLException {
        
        try {
            logger.info(() -> "Rollback transaction " + TRANSACTION_ID.get()
                    + " | " + Thread.currentThread());
        } finally {
            TRANSACTION_ID.remove();
        }
    }
}