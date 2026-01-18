package challenge.concurrency;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class LimitedResource<R> {
    
    private final Semaphore pool;
    private final BlockingQueue<R> queueOfResources;

    public LimitedResource(int capacity, List<R> resources) {
        
        this.pool = new Semaphore(capacity, true);
        
        this.queueOfResources = new LinkedBlockingQueue<>(capacity);
        this.queueOfResources.addAll(resources);
    }

    public R open() throws InterruptedException {
        return open(Long.MAX_VALUE);
    }

    public R open(long timeout) throws InterruptedException {
        
        pool.acquire();
        try {
            return queueOfResources.poll(timeout, TimeUnit.SECONDS);            
        } finally {
            pool.release();
        }
    }

    public void close(R resource) throws InterruptedException {
        
        if (resource != null) {
            queueOfResources.put(resource);
            pool.release();
        }
    }
}