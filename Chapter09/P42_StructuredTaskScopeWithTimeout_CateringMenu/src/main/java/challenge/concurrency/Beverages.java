package challenge.concurrency;

import java.util.List;

public final class Beverages {

    private Beverages() {
        throw new AssertionError("Cannot be instantiated");
    }

    public static List<Beverage> orderBeverages() {
        
        if(Math.random() < 0.1d) { throw new  BeverageException(); }
        
        try { Thread.sleep((long) (Math.random() * 1500)); } catch (InterruptedException ex) {}
               
        return List.of(new Beverage(), new Beverage(), new Beverage());
    }
}
