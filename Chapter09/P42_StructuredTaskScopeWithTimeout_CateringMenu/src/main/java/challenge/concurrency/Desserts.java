package challenge.concurrency;

import java.util.List;

public final class Desserts {

    private Desserts() {
        throw new AssertionError("Cannot be instantiated");
    }

    public static List<Dessert> orderDesserts() {
        
        if(Math.random() < 0.1d) { throw new  DessertException(); }
        
        try { Thread.sleep((long) (Math.random() * 1500)); } catch (InterruptedException ex) {}
                        
        return List.of(new Dessert(), new Dessert());
    }
}
