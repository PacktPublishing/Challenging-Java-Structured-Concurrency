package challenge.concurrency.transaction;

public sealed interface TransactionContext permits TransactionManagement {
    
    static final ScopedValue<String> TRANSACTION_ID = ScopedValue.newInstance();
}
