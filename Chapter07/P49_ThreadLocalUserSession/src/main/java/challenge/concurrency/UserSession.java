package challenge.concurrency;

public class UserSession {   
    
    private static final ThreadLocal<Cart> userCart
            = ThreadLocal.<Cart>withInitial(() -> {
                return new Cart("");
            });
    
    public static void setUserCart(Cart cart) {
        userCart.set(cart);
    }
    
    public static Cart getUserCart() {
        return userCart.get();
    }
    
    public static void clearUserCart() {
        userCart.remove();
    }
}