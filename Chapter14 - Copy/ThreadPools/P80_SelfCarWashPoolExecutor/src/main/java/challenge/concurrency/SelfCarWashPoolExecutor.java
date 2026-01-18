package challenge.concurrency;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class SelfCarWashPoolExecutor extends ThreadPoolExecutor {
 
    private static final Logger logger = Logger.getLogger(SelfCarWashPoolExecutor.class.getName());
    
    private static final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(15);

    private static final class SelfCarWashThreadFactory implements ThreadFactory {

        private static final AtomicInteger counter = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            return Thread.ofPlatform().name("SelfCarWashThread-"
                    + counter.getAndIncrement()).unstarted(r);
        }
    }

    private static final RejectedExecutionHandler rjh = (Runnable r, ThreadPoolExecutor t) -> {
        logger.info("Sorry, no parking left ...");
    };

    public static SelfCarWashPoolExecutor newSelfCarWashPoolExecutor() {
        return new SelfCarWashPoolExecutor(3, 3, 0L, TimeUnit.MILLISECONDS,
                queue, new SelfCarWashThreadFactory(), rjh);
    }

    protected SelfCarWashPoolExecutor(int corePoolSize, int maximumPoolSize,
            long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue,
            ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);        
       
            logger.info(() -> "Getting tokens ..." + Thread.currentThread());
            
        try {
            Thread.sleep((long) (Math.random() * 1000));
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
   }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        
        logger.info(() -> "Free washing space ..." + Thread.currentThread()
                + " | waiting clients: " + queue.size());
    }        
}