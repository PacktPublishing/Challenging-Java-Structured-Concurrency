package challenge.concurrency;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BackgroundExecutor implements AutoCloseable {

    private final ExecutorService executor;

    public BackgroundExecutor() {
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
    }

    public Future<?> executeRunnable(Runnable task) { 

        return executor.submit(task);
    }
    
    public <T> Future<T> executeCallable(Callable<T> task) { 

        return executor.submit(task);
    }
    
    public List<Future<?>> executeRunnables(List<Runnable> tasks) {

        return tasks.stream()
                .map(executor::submit)
                .collect(Collectors.toList());
    }

    public <T> List<Future<T>> executeCallables(List<Callable<T>> tasks) {

        return tasks.stream()
                .map(executor::submit)
                .toList();
    }

    public <T> List<Optional<T>> getResults(
            List<Future<T>> tasks, ExceptionallyInterrupt<T> ei) {

        Function<Future<T>, Optional<T>> func = (task) -> {
            try {
                return Optional.ofNullable(task.get());
            } catch (ExecutionException | InterruptedException e) {
                ei.exception(task, e);
                return Optional.empty();
            }
        };

        return tasks.stream()
                .map(func)
                .toList();
    }

    public <T> Optional<T> getResult(Future<T> task, ExceptionallyInterrupt<T> ei) {

        try {
            return Optional.ofNullable(task.get());
        } catch (ExecutionException | InterruptedException e) {
            ei.exception(task, e);

            return Optional.empty();
        }
    }

    @Override
    public void close() {

        executor.close();
    }

    public boolean shutdownExecutor(
            long timeout, TimeUnit tu, ExceptionallyShutdown es) {

        executor.shutdown();
        try {
            if (!executor.awaitTermination(timeout, tu)) {
                executor.shutdownNow();

                return executor.awaitTermination(timeout, tu);
            }

            return true;

        } catch (InterruptedException e) {
            executor.shutdownNow();
            es.exception(executor, e);
        }

        return false;
    }

    public List<Runnable> shutdownNowExecutor(
            long timeout, TimeUnit tu, ExceptionallyShutdown es) {

        List<Runnable> tasksStillRunning = executor.shutdownNow();

        try {
            executor.awaitTermination(timeout, tu);
        } catch (InterruptedException e) {
            es.exception(executor, e);
        }

        return tasksStillRunning;
    }

    public <T> boolean cancel(Future<T> task) {

        return task.cancel(true);
    }

    public <T> boolean cancel(List<Future<T>> task) {

        return !task.stream()
                .map(f -> f.cancel(true))
                .anyMatch(f -> f.equals(false));
    }

    public static interface ExceptionallyInterrupt<T> {

        void exception(Future<T> t, Exception exception);
    }

    public static interface ExceptionallyShutdown {

        void exception(ExecutorService executor, Exception exception);
    }
}
