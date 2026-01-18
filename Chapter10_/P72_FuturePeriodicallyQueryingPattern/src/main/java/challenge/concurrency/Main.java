package challenge.concurrency;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static class DelayedTask implements Delayed {

        private final long delay;

        public DelayedTask(long delayInMills) {
            this.delay = System.currentTimeMillis() + delayInMills;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            long diff = delay - System.currentTimeMillis();
            return unit.convert(diff, TimeUnit.MICROSECONDS);
        }

        @Override
        public int compareTo(Delayed dt) {

            if (this.delay < ((DelayedTask) dt).delay) {
                return -1;
            }
            
            if (this.delay > ((DelayedTask) dt).delay) {
                return 1;
            }

            return 0;
        }

        public void pingServer() {
          
            logger.info(() -> "Server ping at: " + System.currentTimeMillis());
        }
    }

    public static void main(String[] args) {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        DelayQueue<DelayedTask> delayQueue = new DelayQueue<>();

        for (int i = 1; i < 50; i++) {
            delayQueue.offer(new DelayedTask(i * i * 1000));
        }

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            executor.submit(() -> {
                while (!delayQueue.isEmpty()) {
                    try {
                        DelayedTask delayedTask = delayQueue.take();
                        delayedTask.pingServer();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
    }
}
