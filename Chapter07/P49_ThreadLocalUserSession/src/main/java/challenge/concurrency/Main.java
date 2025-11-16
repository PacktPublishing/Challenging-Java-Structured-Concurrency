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
            try {
                loadUserCartFromDatabase(USER_1);

                printUserCart();
                
                // get the user cart, add two items and remove one
                addCartItem("item4");
                addCartItem("item5");
                removeCartItem("item2");
                
                printUserCart();

                saveUserCartInDatabase();
            } finally {
                clearUserCartInDatabase();
                printUserCart();
            }
        });
        
        Thread user2thread = Thread.ofVirtual().start(() -> { // Thread.ofPlatform()
            try {
                loadUserCartFromDatabase(USER_2);

                printUserCart();
                
                // get the user cart, add two items and remove one
                addCartItem("item4");
                addCartItem("item5");
                removeCartItem("item2");
                
                printUserCart();

                saveUserCartInDatabase();
            } finally {
                clearUserCartInDatabase();
                printUserCart();
            }
        });
        
        user1thread.join();
        user2thread.join();
    }

    public static void loadUserCartFromDatabase(String user) {
        Cart cart = new Cart(user);        
        cart.setItems(new ArrayList<>(Arrays.asList(user + "_item1", user + "_item2", user + "_item3")));
        
        UserSession.setUserCart(cart);
    }
    
    public static void addCartItem(String item) {
        Cart userCart = UserSession.getUserCart();
        userCart.addItem(userCart.getUser() + "_" + item);
    }
    
    public static void removeCartItem(String item) {
        Cart userCart = UserSession.getUserCart();
        userCart.removeItem(userCart.getUser() + "_" + item);
    }

    public static void saveUserCartInDatabase() {
        Cart userCart = UserSession.getUserCart();
        logger.info(() -> "Saving: " + userCart);
        // saving code
    }

    public static void clearUserCartInDatabase() {
        UserSession.clearUserCart();
    }
    
    public static void printUserCart() {
        logger.info(() -> "User cart: " + UserSession.getUserCart().toString());
    }
}
