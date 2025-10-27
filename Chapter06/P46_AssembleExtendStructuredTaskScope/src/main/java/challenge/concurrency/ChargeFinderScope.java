package challenge.concurrency;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNullElse;
import java.util.concurrent.StructuredTaskScope;
import static java.util.concurrent.StructuredTaskScope.Subtask.State.SUCCESS;

public class ChargeFinderScope
        implements StructuredTaskScope.Joiner<Charger, Charger> {

    private volatile PrivateECS privateECS;
    private volatile PublicECS publicECS;
    private volatile PrivateECSException privateECSException;
    private volatile PublicECSException publicECSException;

    @Override
    public boolean onComplete(StructuredTaskScope.Subtask<? extends Charger> subtask) {

        switch (subtask.state()) {
            case SUCCESS -> {
                switch (subtask.get()) {
                    case PrivateECS precs ->
                        this.privateECS = precs;
                    case PublicECS puecs ->
                        this.publicECS = puecs;
                    default ->
                        throw new RuntimeException("Ops!");
                }
            }
            case FAILED -> {
                switch (subtask.exception()) {
                    case PrivateECSException prex ->
                        this.privateECSException = prex;
                    case PublicECSException puex ->
                        this.publicECSException = puex;
                    case Throwable t ->
                        throw new RuntimeException(t);
                }
            }
            case UNAVAILABLE -> {
                throw new IllegalStateException("Subtask may still running ...");
            }
        }
        
        return false;
    }

    @Override
    public Charger result() throws Throwable {
       
        if (nonNull(privateECS) && nonNull(publicECS)) {
            return new ECS(privateECS, publicECS);
        }

        try {
            return requireNonNullElse(privateECS, publicECS);
        } catch (NullPointerException e) {
            throw wrappingECSExceptions();
        }
    }

    private ECSException wrappingECSExceptions() {      

        ECSException exceptionWrapper = new ECSException("No ECS found");
        
        if (privateECSException != null) { exceptionWrapper.addSuppressed(privateECSException); }
        if (publicECSException != null) { exceptionWrapper.addSuppressed(publicECSException); }

        return exceptionWrapper;
    }
}