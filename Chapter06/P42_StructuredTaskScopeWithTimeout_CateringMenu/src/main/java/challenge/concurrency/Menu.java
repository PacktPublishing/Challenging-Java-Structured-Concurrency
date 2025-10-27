package challenge.concurrency;

import java.util.List;

public record Menu(Starter starter, MainCourse mainCourse, 
        List<Dessert> desserts, List<Beverage> Beverages) {}
