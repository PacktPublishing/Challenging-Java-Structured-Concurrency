package challenge.concurrency;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.StructuredTaskScope;
import static java.util.concurrent.StructuredTaskScope.Subtask.State.FAILED;
import static java.util.concurrent.StructuredTaskScope.Subtask.State.SUCCESS;
import static java.util.concurrent.StructuredTaskScope.Subtask.State.UNAVAILABLE;

public class PublicChargeFinderScope
        implements StructuredTaskScope.Joiner<List<PublicECS>, PublicECS> {

    private static final int GOLDEN_TIME = 25;

    private final List<List<PublicECS>> ecsResults = new CopyOnWriteArrayList<>();
    private final List<Throwable> ecsExceptions = new CopyOnWriteArrayList<>();

    @Override
    public boolean onComplete(StructuredTaskScope.Subtask<? extends List<PublicECS>> subtask) {

        switch (subtask.state()) {
            case SUCCESS -> {
                List<PublicECS> publicEcs = subtask.get();
                ecsResults.add(publicEcs);
                for (PublicECS pEcs : publicEcs) {
                    if (pEcs.availableInMin() < GOLDEN_TIME) {                        
                        return true;
                    }
                }
            }
            case FAILED ->
                ecsExceptions.add(subtask.exception());
            case UNAVAILABLE -> {
                throw new IllegalStateException("Subtask may still running ...");
            }
        }

        return false;
    }

    @Override
    public PublicECS result() throws Throwable {

        return ecsResults.stream()
                .flatMap(t -> t.stream())
                .min(Comparator.comparing(PublicECS::availableInMin))
                .orElseThrow(this::wrappingECSExceptions);
    }

    private PublicECSException wrappingECSExceptions() {

        PublicECSException exceptionWrapper
                = new PublicECSException("Public ECS exception");        
        ecsExceptions.forEach(exceptionWrapper::addSuppressed);

        return exceptionWrapper;
    }
}