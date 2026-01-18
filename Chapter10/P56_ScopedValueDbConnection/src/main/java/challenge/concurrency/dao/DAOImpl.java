package challenge.concurrency.dao;

import java.util.logging.Logger;

public final class DAOImpl implements DAO {

    private static final Logger logger = Logger.getLogger(DAOImpl.class.getName());

    @Override
    public void sqlOperation1() {

        ScopedValue.where(DB_CONN, DatabaseConnection.openDbConnection()).run(() -> {
            try {                
                logger.info(() -> "Executing SQL operation 1 using connection: "
                        + DB_CONN.get() + " by " + Thread.currentThread().toString());
            } finally {
                DatabaseConnection.closeDbConnection(DB_CONN.get());
            }
        });
    }

    @Override
    public void sqlOperation2() {

        ScopedValue.where(DB_CONN, DatabaseConnection.openDbConnection()).run(() -> {
            try {
                logger.info(() -> "Executing SQL operation 2 using connection: "
                        + DB_CONN.get() + " by " + Thread.currentThread().toString());
            } finally {
                DatabaseConnection.closeDbConnection(DB_CONN.get());
            }
        });
    }
}
