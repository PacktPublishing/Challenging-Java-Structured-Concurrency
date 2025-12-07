package challenge.concurrency;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.FailedException;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.StructuredTaskScope.TimeoutException;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Menu menu = orderCateringMenu(Duration.ofSeconds(1));

        logger.info(menu.toString());
    }

    public static Menu orderCateringMenu(Duration timeout) throws InterruptedException {

        // StructuredTaskScope<Object, Void>
        try (var scope = StructuredTaskScope.open(Joiner.awaitAllSuccessfulOrThrow(), 
                cf -> cf.withTimeout(timeout))) {

            Subtask<Starter> starterSubtask = scope.fork(() -> Starters.orderStarter());
            Subtask<MainCourse> mainCourseSubtask = scope.fork(() -> MainCourses.orderMainCourse());
            Subtask<List<Dessert>> dessertsSubtask = scope.fork(() -> Desserts.orderDesserts());
            Subtask<List<Beverage>> beveragesSubtask = scope.fork(() -> Beverages.orderBeverages());

            scope.join(); // Join subtasks, propagating exceptions

            // All subtasks have succeeded, so compose their results
            Menu menu = new Menu(starterSubtask.get(), mainCourseSubtask.get(),
                    dessertsSubtask.get(), beveragesSubtask.get());

            return menu;
        } catch (FailedException ex) {

            Throwable cause = ex.getCause();
            logger.severe(cause.toString());
            
            throw new MenuException("Sorry, we are unable to provide the requested menu due to internal issues");
        } catch (TimeoutException ex) {           
            logger.severe(ex.toString());
            
            throw new MenuException("Sorry, we cannot provide on time the requested menu");            
        }
    }
}
