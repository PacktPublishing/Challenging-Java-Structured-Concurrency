package challenge.concurrency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final String USER_1 = "Mike1989";
    private static final String USER_2 = "LionKing";

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        Thread user1thread = Thread.ofVirtual().start(() -> { // Thread.ofPlatform()
           
            ScopedValue.where(UserSession.CART, loadUserCartFromDatabase(USER_1)).run(() -> {
                printUserCart();

                // get the user cart, add two items and remove one
                addCartItem("item4");
                addCartItem("item5");
                removeCartItem("item2");

                printUserCart();

                saveUserCartInDatabase();
            });
        });

        Thread user2thread = Thread.ofVirtual().start(() -> { // Thread.ofPlatform()           

            ScopedValue.where(UserSession.CART, loadUserCartFromDatabase(USER_2)).run(() -> {
                printUserCart();

                // get the user cart, add two items and remove one
                addCartItem("item4");
                addCartItem("item5");
                removeCartItem("item2");

                printUserCart();

                saveUserCartInDatabase();
            });
        });

        user1thread.join();
        user2thread.join();
        
        printUserCart();
    }

    public static Cart loadUserCartFromDatabase(String user) {
        Cart cart = new Cart(user);
        cart.setItems(new ArrayList<>(Arrays.asList(user + "_item1", user + "_item2", user + "_item3")));

        return cart;
    }

    public static void addCartItem(String item) {
        Cart userCart = UserSession.CART.get();
        userCart.addItem(userCart.getUser() + "_" + item);
    }

    public static void removeCartItem(String item) {
        Cart userCart = UserSession.CART.get();
        userCart.removeItem(userCart.getUser() + "_" + item);
    }

    public static void saveUserCartInDatabase() {
        Cart userCart = UserSession.CART.get();
        logger.info(() -> "Saving: " + userCart);
        // saving code
    }
    
    public static void printUserCart() {
        logger.info(() -> "User cart: " + UserSession.CART.get().toString());
    }
}