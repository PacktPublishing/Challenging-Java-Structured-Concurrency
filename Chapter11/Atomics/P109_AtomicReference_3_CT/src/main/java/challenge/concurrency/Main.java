package challenge.concurrency;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
   
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");                        
                       
        // BadInterval interval = new BadInterval();
        CasStack<String> stack = new CasStack<>();
        
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            
            for(int i = 0; i < 100; i++) {
                executor.submit(() -> stack.push("str-" + Math.random()));
                executor.submit(() -> stack.pop());               
            }
        }        
        
        logger.info(stack.pop());        
    }
}
