package challenge.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    private static final ScopedValue<String> SV = ScopedValue.newInstance();

    public static void main(String[] args) {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");        
        
        Runnable runnableTask = () -> {
            logger.info(() -> Thread.currentThread().toString() 
                    + " | before pause | " + (SV.isBound() ? SV.get() : "Not bound"));
            
            try { Thread.sleep((long) (Math.random() * 5000)); } catch (InterruptedException ex) {}
            
            logger.info(() -> Thread.currentThread().toString() 
                    + " | after pause | " + (SV.isBound() ? SV.get() : "Not bound"));        
        };
        
        // newThreadPerTaskExecutor(Thread.ofPlatform().factory())
        try (ExecutorService executor = Executors
                .newVirtualThreadPerTaskExecutor()) {

            for (int i = 0; i < 10; i++) {
                int index = i;
                executor.submit(() -> ScopedValue.where(
                        SV, "Bumfuzzle-" + index).run(runnableTask));
            }
        }
    }  
}