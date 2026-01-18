package challenge.concurrency;

public class Main {
   
    public static void main(String[] args) throws InterruptedException {

        NewVirtualThread nvt = new NewVirtualThread();
        nvt.newVirtualThread();

        Thread.sleep(2000);
        
        RunnableVirtualThread rvt = new RunnableVirtualThread();
        rvt.runnableVirtualThread();
        
        Thread.sleep(2000);
        
        WaitVirtualThread wvt = new WaitVirtualThread();
        wvt.waitingVirtualThread();
        
        Thread.sleep(2000);
        
        TimedWaitingVirtualThread twvt = new TimedWaitingVirtualThread();
        twvt.timedWaitingVirtualThread();
        
        Thread.sleep(2000);
        
        TerminatedVirtualThread tvt = new TerminatedVirtualThread();
        tvt.terminatedVirtualThread();
        
        Thread.sleep(2000);
        
       BlockedVirtualThread bvt = new BlockedVirtualThread();
       bvt.blockedVirtualThread();
    }
}