package challenge.concurrency;

import java.util.concurrent.FutureTask;

public class LogFutureTask extends FutureTask {
    
    public LogFutureTask(Runnable runnable, String result) {
        super(runnable, result);
    }            

    @Override
    protected boolean runAndReset() {
        return super.runAndReset();
    }        
}