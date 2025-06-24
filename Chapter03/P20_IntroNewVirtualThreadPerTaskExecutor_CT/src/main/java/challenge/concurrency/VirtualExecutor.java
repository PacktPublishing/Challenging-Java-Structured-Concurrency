package challenge.concurrency;

import java.util.concurrent.Executor;

public class VirtualExecutor implements Executor {

    @Override
    public void execute(Runnable command) {
        try {
            Thread.startVirtualThread(command).join();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt(); // log "ex"
        }
    }
    
}
