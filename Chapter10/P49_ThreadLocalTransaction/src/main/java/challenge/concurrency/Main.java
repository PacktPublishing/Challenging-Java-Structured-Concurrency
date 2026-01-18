package challenge.concurrency;

import challenge.concurrency.transaction.TransactionManagement;
import java.sql.SQLException;

public class Main {
  
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Thread transaction1Thread = Thread.ofVirtual().start(() -> { // Thread.ofPlatform()                            

            try {
                TransactionManagement.begin();
                TransactionManagement.commit();
            } catch (SQLException ex) {
                try {
                    TransactionManagement.rollback();
                } catch (SQLException ex1) {
                    // handle rollback exception
                }
            }
        });

        Thread transaction2Thread = Thread.ofVirtual().start(() -> { // Thread.ofPlatform()                            

            try {
                TransactionManagement.begin();
                TransactionManagement.commit();
            } catch (SQLException ex) {
                try {
                    TransactionManagement.rollback();
                } catch (SQLException ex1) {
                    // handle rollback exception
                }
            }

        });

        transaction1Thread.join();
        transaction2Thread.join();
    }
}
