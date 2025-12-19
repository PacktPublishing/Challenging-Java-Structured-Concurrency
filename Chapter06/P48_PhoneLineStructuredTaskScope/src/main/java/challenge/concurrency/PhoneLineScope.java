package challenge.concurrency;

import java.util.concurrent.StructuredTaskScope;
import static java.util.concurrent.StructuredTaskScope.Subtask.State.FAILED;
import static java.util.concurrent.StructuredTaskScope.Subtask.State.SUCCESS;
import static java.util.concurrent.StructuredTaskScope.Subtask.State.UNAVAILABLE;
import java.util.logging.Logger;

public class PhoneLineScope
        implements StructuredTaskScope.Joiner<Operator, Operator> {
    
    private static final Logger logger = Logger.getLogger(PhoneLineScope.class.getName());

    private volatile Operator result;
    private volatile Throwable exception;

    @Override
    public boolean onComplete(StructuredTaskScope.Subtask<Operator> subtask) {

        switch (subtask.state()) {
            case SUCCESS ->
                result = subtask.get();
            case FAILED ->
                exception = subtask.exception();
            case UNAVAILABLE -> {
                throw new IllegalStateException("Subtask may still running ...");
            }
        }

        return true;
    }

    @Override
    public Operator result() throws Throwable {

        if (result != null) { 
            return result;
        }
        
        throw exception;
    }

    @Override
    public void onTimeout() {
        
        logger.warning(() -> "All human operators are busy ...");
        result = new RobotOperator();
    }       
}