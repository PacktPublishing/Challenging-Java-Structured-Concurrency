package challenge.concurrency.transaction;

sealed interface TransactionContext permits TransactionManagement {
    
    static final ThreadLocal<String> TRANSACTION_ID = ThreadLocal.withInitial(
            () -> IdGenerator.fetchStringId());
}
