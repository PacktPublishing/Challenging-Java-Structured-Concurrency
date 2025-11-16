package challenge.concurrency.dao;

import java.sql.Connection;
import static java.sql.DriverManager.getConnection;
import java.sql.SQLException;

final class DatabaseConnection {   
    
    private DatabaseConnection() {
        throw new AssertionError("Cannot be instantiated");
    }
    
    static final ThreadLocal<Connection> dbConn = ThreadLocal.withInitial(() -> {
        try {
            return getConnection("jdbc:oracle:thin:@localhost:1521:xe", "root", "root");
        } catch (SQLException ex) {
            throw new RuntimeException("Cannot connect to the database", ex);
        }
    });
    
    static Connection openDbConnection() {
        return dbConn.get();
    }
    
    static void closeDbConnection() {
        try {
            dbConn.get().close();
        } catch (SQLException e) {
            // handle this
        } finally {
            dbConn.remove();
        }
    }
}