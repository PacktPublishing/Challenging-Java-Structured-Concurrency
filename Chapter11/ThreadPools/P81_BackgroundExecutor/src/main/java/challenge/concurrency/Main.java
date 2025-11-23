package challenge.concurrency;

import challenge.concurrency.BackgroundExecutor.ExceptionallyInterrupt;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static class RunnableException implements ExceptionallyInterrupt {

        @Override
        public void exception(Future t, Exception e) {
            logger.severe(() -> "Runnable | Future: " + t + " Excepption: " + e);
        }
    }
    
    public static class CallableException implements ExceptionallyInterrupt {

        @Override
        public void exception(Future t, Exception e) {
            logger.severe(() -> "Callable | Future: " + t + " Excepption: " + e);
        }
    }

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Runnable voidTask = () -> {
            try { Thread.sleep(1000); } catch (InterruptedException ex) {}
            logger.info(() -> "Runnable:" + Thread.currentThread().toString());
        };

        Callable<Boolean> returnTask = () -> {
            try { Thread.sleep(1000); } catch (InterruptedException ex) {}
            logger.info(() -> "Callable: " + Thread.currentThread().toString());
            return Math.random() < 0.5d;
        };

        try (BackgroundExecutor executor = new BackgroundExecutor()) {

            Future<?> runnable = executor.executeRunnable(voidTask);
            Future<Boolean> callable = executor.executeCallable(returnTask);
            
            Optional resultRunnable = executor.getResult(runnable, new RunnableException());
            Optional<Boolean> resultCallable = executor.getResult(callable, new CallableException());
            
            logger.info(resultRunnable.orElse("no result").toString());
            logger.info(resultCallable.orElse(null).toString());
        }
    }

}
