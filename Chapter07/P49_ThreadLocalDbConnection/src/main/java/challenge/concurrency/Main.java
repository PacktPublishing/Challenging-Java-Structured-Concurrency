package challenge.concurrency;

import challenge.concurrency.dao.DAOImpl;

public class Main {
   
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");                
        
        DAOImpl dao = new DAOImpl();
        
        Thread dbClient1Thread = Thread.ofVirtual().start(() -> { // Thread.ofPlatform()
                                               
            dao.sqlOperation1();
            dao.sqlOperation2();
        });
        
        Thread dbClient2Thread = Thread.ofVirtual().start(() -> { // Thread.ofPlatform()                      
            
            dao.sqlOperation1();
            dao.sqlOperation2();
        });
        
        dbClient1Thread.join();
        dbClient2Thread.join();
    }   
}
