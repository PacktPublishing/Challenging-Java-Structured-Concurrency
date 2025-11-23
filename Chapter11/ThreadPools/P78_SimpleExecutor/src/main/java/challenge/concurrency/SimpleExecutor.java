package challenge.concurrency;

import java.util.concurrent.Executor;

public class SimpleExecutor implements Executor {

    @Override
    public void execute(Runnable r) {
        Thread.ofPlatform().start(r);
    }

    public Thread executeVirtualThread(Runnable r) {
        return Thread.ofVirtual().start(r);
    }
}
