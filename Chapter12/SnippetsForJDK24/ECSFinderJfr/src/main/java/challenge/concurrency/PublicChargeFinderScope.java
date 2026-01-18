package challenge.concurrency;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.ThreadFactory;

public class PublicChargeFinderScope extends StructuredTaskScope<List<PublicECS>> {

    private static final int GOLDEN_TIME = 25;
    
    private final List<List<PublicECS>> ecsResults = new CopyOnWriteArrayList<>();
    private final List<Throwable> ecsExceptions = new CopyOnWriteArrayList<>();

    public PublicChargeFinderScope(String name, ThreadFactory factory) {
        super(name, factory);
    }        
    
    @Override
    protected void handleComplete(Subtask<? extends List<PublicECS>> subtask) {

        switch (subtask.state()) {
            case SUCCESS -> {
                List<PublicECS> publicEcs = subtask.get();  
                ecsResults.add(publicEcs);
                for(PublicECS pEcs : publicEcs) {
                    if(pEcs.availableInMin() < GOLDEN_TIME) {
                        super.shutdown();
                        break;                                                                        
                    }
                }                                
            }
            case FAILED ->
                ecsExceptions.add(subtask.exception());
            case UNAVAILABLE ->{                
                throw new IllegalStateException("Subtask may still running ...");
            }
        }
    }
    
    public PublicECS fastestPublicECS() {
        
        super.ensureOwnerAndJoined();
        
        return ecsResults.stream()
                .flatMap(t -> t.stream())
                .min(Comparator.comparing(PublicECS::availableInMin))
                .orElseThrow(this::wrappingECSExceptions);
    }
    
    private PublicECSException wrappingECSExceptions() {

        super.ensureOwnerAndJoined();
        
        PublicECSException exceptionWrapper 
                = new PublicECSException("Public ECS exception");
        ecsExceptions.forEach(exceptionWrapper::addSuppressed);

        return exceptionWrapper;
    }
}