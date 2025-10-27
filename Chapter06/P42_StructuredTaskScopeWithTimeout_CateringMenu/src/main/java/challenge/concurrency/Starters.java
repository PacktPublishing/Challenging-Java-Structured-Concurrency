package challenge.concurrency;

public final class Starters {

    private Starters() {
        throw new AssertionError("Cannot be instantiated");
    }

    public static Starter orderStarter() {
        
        if(Math.random() < 0.1d) { throw new  StarterException(); }
        
        try { Thread.sleep((long) (Math.random() * 1500)); } catch (InterruptedException ex) {}
        
        return new Starter();
    }
}
