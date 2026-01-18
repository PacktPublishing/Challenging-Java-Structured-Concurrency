package challenge.concurrency;

import java.util.concurrent.Executors;
import java.util.concurrent.StructuredTaskScope.Joiner;
import static java.util.concurrent.StructuredTaskScope.open;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    // using a custom thread factory
    private static class PlatformThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {

            return Thread.ofPlatform().unstarted(r);
        }
    }

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        // default thread factory
        try (var scope = open(Joiner.<Object>allSuccessfulOrThrow())) {

            scope.fork(() -> logger.info(Thread.currentThread().toString()));

            scope.join();
        }

        // custom factory of virtual threads        
        try (var scope = open(Joiner.<Object>allSuccessfulOrThrow(),
                cf -> cf.withThreadFactory(Thread.ofVirtual().name("kaboom-", 1).factory()))) {

            scope.fork(() -> logger.info(Thread.currentThread().toString()));

            scope.join();
        }

        // using the default thread factory provided by Executors
        try (var scope = open(Joiner.<Object>allSuccessfulOrThrow(),
                cf -> cf.withThreadFactory(Executors.defaultThreadFactory()))) {

            scope.fork(() -> logger.info(Thread.currentThread().toString()));

            scope.join();
        }

        // using the Thread.ofPlatform().factory()
        try (var scope = open(Joiner.<Object>allSuccessfulOrThrow(),
                cf -> cf.withThreadFactory(Thread.ofPlatform().name("platformThread-", 0).factory()))) {

            scope.fork(() -> logger.info(Thread.currentThread().toString()));            

            scope.join();
        }

        // using a custom thread factory implementation
        try (var scope = open(Joiner.<Object>allSuccessfulOrThrow(),
                cf -> cf.withThreadFactory(new PlatformThreadFactory()))) {

            scope.fork(() -> logger.info(Thread.currentThread().toString()));

            scope.join();
        }
    }
}
