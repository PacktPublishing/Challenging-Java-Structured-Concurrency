package challenge.concurrency.dao;

import java.sql.Connection;

sealed interface DAO permits DAOImpl {
    
    static final ScopedValue<Connection> DB_CONN = ScopedValue.newInstance();
    
    public void sqlOperation1();
    public void sqlOperation2();
}
