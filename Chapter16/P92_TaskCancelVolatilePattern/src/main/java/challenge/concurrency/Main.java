package challenge.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
   
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        CancellableTask task = new CancellableTask();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            for (int i = 0; i < 10; i++) {
                executor.execute(task);
            }

            Thread.sleep(150);

            task.cancel();
        }
    }
}
