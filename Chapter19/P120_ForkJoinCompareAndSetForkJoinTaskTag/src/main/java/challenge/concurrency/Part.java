package challenge.concurrency;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.RecursiveTask;
import java.util.logging.Logger;

public class Part<Integer> extends RecursiveTask<Integer> {

    private static final Logger logger = Logger.getLogger(Part.class.getName());

    private static final short UNVISITED = 0;
    private static final short VISITED = 1;

    private final String name;
    private final Callable<Integer> partcall;
    private Set<Part<Integer>> siblings = new HashSet<>();

    @SuppressWarnings("unchecked")
    public Part(String name, Callable<Integer> partcall, Part<Integer>... siblings) {
        this.name = name;
        this.partcall = partcall;
        this.siblings = Set.of(siblings);
    }

    @Override
    protected Integer compute() {

        siblings.stream()
                .filter(p -> p.updatePartToVisited())
                .forEachOrdered(p -> {
                    logger.info(() -> "Tagged: " + p
                            + "(" + p.getForkJoinTaskTag() + ")");
                    p.fork();
                });

        for (Part p : siblings) {
            p.join();
        }

        try {
            return partcall.call();
        } catch (Exception e) {
            logger.severe(() -> "Partcall Exception: " + e);
        }

        return null;
    }

    public boolean updatePartToVisited() {
        return compareAndSetForkJoinTaskTag(UNVISITED, VISITED);
    }

    @Override
    public String toString() {
        return name;
    }
}
