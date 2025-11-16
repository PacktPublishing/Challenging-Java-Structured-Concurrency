package challenge.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final User user = new User();
    
    public static void main(String[] args) throws InterruptedException {
        
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
       
        try (ExecutorService executor
                = Executors.newVirtualThreadPerTaskExecutor()) { // newThreadPerTaskExecutor(Executors.defaultThreadFactory()
            for (int i = 0; i < 10; i++) {                
                executor.submit(user);
            }
        }                
    }
}
