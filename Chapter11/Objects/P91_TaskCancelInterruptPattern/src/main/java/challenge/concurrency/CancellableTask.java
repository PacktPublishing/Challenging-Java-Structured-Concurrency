package challenge.concurrency;

import java.util.logging.Logger;

public class CancellableTask {
    
    private static final Logger logger = Logger.getLogger(CancellableTask.class.getName());
    
    private Thread thread;

    private final Runnable task = () -> {
        while (!Thread.currentThread().isInterrupted()) {
            logger.info("Task running ...");
            // task code
        }
    };

    public void run(boolean isVirtual) {
        
        thread = isVirtual ? Thread.ofVirtual().unstarted(task) 
                : Thread.ofPlatform().unstarted(task); 
        
        thread.start();
    }
    
    public boolean isAlive() {
        
        return thread.isAlive();
    }
    
    public boolean isInterrupted() {
        
        return thread.isInterrupted();
    }

    public void cancel() {
        
        if (thread != null) {
            thread.interrupt();
        }
    }
}