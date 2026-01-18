package challenge.concurrency;

public class Main {
   
    public static void main(String[] args) throws InterruptedException {

        NewPlatformThread npt = new NewPlatformThread();
        npt.newPlatformThread();

        Thread.sleep(2000);
        
        RunnablePlatformThread pvt = new RunnablePlatformThread();
        pvt.runnablePlatformThread();
                
        Thread.sleep(2000);
        
        WaitPlatformThread wpt = new WaitPlatformThread();
        wpt.waitingPlatformThread();
        
        Thread.sleep(2000);
        
        TimedWaitingPlatformThread twpt = new TimedWaitingPlatformThread();
        twpt.timedWaitingPlatformThread();
        
        Thread.sleep(2000);
        
        TerminatedPlatformThread tpt = new TerminatedPlatformThread();
        tpt.terminatedPlatformThread();
        
        Thread.sleep(2000);
        
        BlockedPlatformThread bpt = new BlockedPlatformThread();
        bpt.blockedPlatformThread();
    }
}