package challenge.concurrency;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.StructuredTaskScope;
import static java.util.concurrent.StructuredTaskScope.Subtask.State.FAILED;
import static java.util.concurrent.StructuredTaskScope.Subtask.State.SUCCESS;
import static java.util.concurrent.StructuredTaskScope.Subtask.State.UNAVAILABLE;

public class PrivateChargeFinderScope extends StructuredTaskScope<PrivateECS> {
    
    private final List<PrivateECS> ecsResults = new CopyOnWriteArrayList<>();
    private final List<Throwable> ecsExceptions = new CopyOnWriteArrayList<>();

    @Override
    protected void handleComplete(Subtask<? extends PrivateECS> subtask) {
        switch (subtask.state()) {
            case SUCCESS ->               
                ecsResults.add(subtask.get());                
            case FAILED ->
                ecsExceptions.add(subtask.exception());
            case UNAVAILABLE ->{                
                throw new IllegalStateException("Subtask may still running ...");
            }
        }
    }
    
    public PrivateECS cheapestPrivateECS() {
        
        super.ensureOwnerAndJoined();
        
        return ecsResults.stream()                
                .min(Comparator.comparing(PrivateECS::pricePerMin))
                .orElseThrow(this::wrappingECSExceptions);
    }
    
    private PrivateECSException wrappingECSExceptions() {

        super.ensureOwnerAndJoined();
        
        PrivateECSException exceptionWrapper 
                = new PrivateECSException("Private ECS exception");
        ecsExceptions.forEach(exceptionWrapper::addSuppressed);

        return exceptionWrapper;
    }        
}
