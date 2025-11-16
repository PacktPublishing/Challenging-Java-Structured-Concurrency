package challenge.concurrency.dao;

import java.util.logging.Logger;

public final class DAOImpl implements DAO {

    private static final Logger logger = Logger.getLogger(DAOImpl.class.getName());

    @Override
    public void sqlOperation1() {

        try {
            DatabaseConnection.openDbConnection();

            logger.info(() -> "Executing SQL operation 1 using connection: "
                    + DatabaseConnection.dbConn.get() + " by " + Thread.currentThread().toString());
        } finally {
            DatabaseConnection.closeDbConnection();
        }
    }

    @Override
    public void sqlOperation2() {

        try {
            DatabaseConnection.openDbConnection();

            logger.info(() -> "Executing SQL operation 2 using connection: "
                    + DatabaseConnection.dbConn.get() + " by " + Thread.currentThread().toString());
        } finally {
            DatabaseConnection.closeDbConnection();
        }
    }
}
